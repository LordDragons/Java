package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Date;

public class ParkingService {

    private static final Logger logger = LogManager.getLogger("ParkingService");

    private static final FareCalculatorService fareCalculatorService = new FareCalculatorService();

    private final InputReaderUtil inputReaderUtil;
    private final ParkingSpotDAO parkingSpotDAO;
    private final TicketDAO ticketDAO;

    public ParkingService(InputReaderUtil inputReaderUtil, ParkingSpotDAO parkingSpotDAO, TicketDAO ticketDAO){
        this.inputReaderUtil = inputReaderUtil;
        this.parkingSpotDAO = parkingSpotDAO;
        this.ticketDAO = ticketDAO;
    }

    public void processIncomingVehicle() {
        try {
            // Récupérer la place de parking disponible
            ParkingSpot parkingSpot = getNextParkingNumberIfAvailable();
            if (parkingSpot != null && parkingSpot.getId() > 0) {
                // Demander le numéro d'immatriculation du véhicule
                String vehicleRegNumber = getVehicleRegNumber();

                // Mettre à jour l'état de la place de parking
                parkingSpot.setAvailable(false);
                parkingSpotDAO.updateParking(parkingSpot);

                Date inTime = new Date();
                Ticket ticket = new Ticket();
                ticket.setParkingSpot(parkingSpot);
                ticket.setVehicleRegNumber(vehicleRegNumber);
                ticket.setPrice(0); // ou une logique pour calculer le prix
                ticket.setInTime(inTime);
                ticket.setOutTime((Date) null); // Pas encore sorti

                // Sauvegarder le ticket dans la base de données
                boolean isSaved = ticketDAO.saveTicket(ticket);
                if (isSaved) {
                    logger.info("Ticket generated and saved in DB for vehicle: {}", vehicleRegNumber);
                } else {
                    logger.error("Failed to save ticket for vehicle: {}", vehicleRegNumber);
                }

                // Afficher des informations supplémentaires
                System.out.println("Please park your vehicle in spot number: " + parkingSpot.getId());
                System.out.println("Recorded in-time for vehicle number: " + vehicleRegNumber + " is: " + inTime);
            }
        } catch (Exception e) {
            logger.error("Unable to process incoming vehicle", e);
        }
    }


    private String getVehicleRegNumber() {
        return "ABCPDF";
    }


    public ParkingSpot getNextParkingNumberIfAvailable() throws Exception {
        ParkingSpot parkingSpot = null;
        try {
            ParkingType parkingType = getVehicleType();

            if (parkingType == null) {
                throw new IllegalArgumentException("Entered input is invalid");
            }

            int parkingNumber = parkingSpotDAO.getNextAvailableSlot(parkingType);

            // Vérification pour voir si la place de parking est disponible
            if (parkingNumber <= 0) {  // Si aucune place n'est disponible
                throw new Exception("Error fetching parking number from DB. Parking slots might be full");
            }

            parkingSpot = new ParkingSpot(parkingNumber, parkingType, true);

        } catch (IllegalArgumentException ie) {
            logger.error("Error parsing user input for type of vehicle", ie);
            throw ie;  // Relancer l'exception pour que le test puisse la capturer
        } catch (Exception e) {
            logger.error("Error fetching next available parking slot", e);
            throw e;  // Relancer l'exception pour qu'elle puisse être capturée par le test
        }
        return parkingSpot;
    }



    public ParkingType getVehicleType(){
        System.out.println("Please select vehicle type from menu");
        System.out.println("1 CAR");
        System.out.println("2 BIKE");
        int input = inputReaderUtil.readSelection();
        switch(input){
            case 1: {
                return ParkingType.CAR;
            }
            case 2: {
                return ParkingType.BIKE;
            }
            default: {
                System.out.println("Incorrect input provided");
                throw new IllegalArgumentException("Entered input is invalid");
            }
        }
    }

    public void processExitingVehicle() {
        try {
            String vehicleRegNumber = getVehicleRegNumber();
            Ticket ticket = ticketDAO.getTicket(vehicleRegNumber);

            if (ticket == null) {
                logger.error("No ticket found for vehicle registration number: " + vehicleRegNumber);
                System.out.println("No ticket found for vehicle registration number: " + vehicleRegNumber);
                return; // Fin de la méthode si aucun ticket n'est trouvé
            }

            Date outTime = new Date();
            ticket.setOutTime(outTime);
            fareCalculatorService.calculateFare(ticket);

            if (ticketDAO.updateTicket(ticket)) {
                ParkingSpot parkingSpot = ticket.getParkingSpot();
                parkingSpot.setAvailable(true);
                parkingSpotDAO.updateParking(parkingSpot);
                System.out.println("Please pay the parking fare:" + ticket.getPrice());
                System.out.println("Recorded out-time for vehicle number:" + ticket.getVehicleRegNumber() + " is:" + outTime);
            } else {
                System.out.println("Unable to update ticket information. Error occurred");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
