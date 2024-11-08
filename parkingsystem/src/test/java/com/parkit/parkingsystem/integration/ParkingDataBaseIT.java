package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static com.parkit.parkingsystem.integration.config.DataBaseTestConfig.logger;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static Ticket ticket;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    public static void setUp() {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticket = new Ticket();
        ticket.setVehicleRegNumber("ABCPDF");
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    public void setUpPerTest() {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCPDF");
        dataBasePrepareService.clearDataBaseEntries();
        dataBasePrepareService.populateTestParkingSpots();
    }

    @AfterAll
    public static void tearDown() {
        // Clean up resources after all tests, if needed
    }

    @Test
    public void testParkingACar() {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();

        // Récupérer le ticket et vérifier s'il n'est pas null
        Ticket savedTicket = ticketDAO.getLatestTicket();
        assertNotNull(savedTicket, "Le ticket enregistré ne doit pas être null");

        // Vérifier la plaque d'immatriculation du véhicule
        assertEquals("ABCPDF", savedTicket.getVehicleRegNumber());

        // Vérifier que la place de parking a bien été mise à jour
        int updatedSpot = parkingSpotDAO.getNextAvailableSlot(savedTicket.getParkingSpot().getParkingNumber());
        assertTrue(updatedSpot > 0);
    }



    @Test
    public void testParkingLotExit() {
        testParkingACar();

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();

        Ticket exitedTicket = ticketDAO.getTicketByLicensePlate("ABCPDF");
        assertNotNull(exitedTicket);
        assertNotNull(exitedTicket.getOutTime());
    }

    @Test
    public void testRecurrentVehicleDiscount() {

        // First vehicle entry
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("RECUR123");
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        parkingService.processExitingVehicle();

        // Second vehicle entry for recurrent vehicle
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("RECUR123");
        parkingService.processIncomingVehicle();

        // Retrieve the latest ticket and validate it's not null
        Ticket savedTicket = ticketDAO.getLatestTicket();
        assertNotNull(savedTicket, "Ticket should not be null");

        // Validate the vehicle registration number and price (apply discount)
        assertEquals("RECUR123", savedTicket.getVehicleRegNumber());
        assertTrue(savedTicket.getPrice() < 10, "Price should be below 10");

        // Log ticket details for further debugging
        logger.info("Saved ticket: {}", savedTicket);
    }



    @Test
    public void testNoAvailableParkingSpot() {
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("NO_SPACE");

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        dataBasePrepareService.fillAllParkingSpots();

        parkingService.processIncomingVehicle();

        Ticket savedTicket = ticketDAO.getLatestTicket();
        assertNull(savedTicket);
    }

    @Test
    public void testInvalidVehicleRegistrationNumber() {
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("INVALID");

        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();

        Ticket savedTicket = ticketDAO.getLatestTicket();
        assertNull(savedTicket);
    }
}
