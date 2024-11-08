package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static ParkingService parkingService;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeEach
    public void setUpPerTest() throws Exception {
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
        Ticket ticket = new Ticket();
        ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCPDF");

        lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCPDF");
        lenient().when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        lenient().when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
        lenient().when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);


        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
    }

    // Test existant : Cas où le véhicule sort normalement
    @Test
    public void processExitingVehicleTest() {
        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    // Test existant : Cas où un véhicule entre normalement
    @Test
    public void processIncomingVehicleTest() {
        try {
            when(inputReaderUtil.readSelection()).thenReturn(1); // CAR
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);

            parkingService.processIncomingVehicle();

            verify(parkingSpotDAO, times(1)).updateParking(any(ParkingSpot.class));
            verify(ticketDAO, times(1)).saveTicket(any(Ticket.class));
        } catch (Exception e) {
            fail("Exception in processIncomingVehicleTest: " + e.getMessage());
        }
    }

    // Cas où aucune place de parking n'est disponible
    @Test
    public void processIncomingVehicle_NoParkingSpotAvailable_ShouldLogError() {
        try {
            when(inputReaderUtil.readSelection()).thenReturn(1); // CAR
            when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(-1);

            parkingService.processIncomingVehicle();

            verify(parkingSpotDAO, never()).updateParking(any(ParkingSpot.class));
            verify(ticketDAO, never()).saveTicket(any(Ticket.class));
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }
    }

    // Exception lors de la récupération du numéro de plaque d’immatriculation
    @Test
    public void processIncomingVehicle_ExceptionOnVehicleRegNumber_ShouldLogError() {
        lenient().when(inputReaderUtil.readSelection()).thenReturn(1); // CAR
        lenient().when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenThrow(new RuntimeException("Error reading vehicle reg number"));

        parkingService.processIncomingVehicle();

        verify(parkingSpotDAO, never()).updateParking(any(ParkingSpot.class));
        verify(ticketDAO, never()).saveTicket(any(Ticket.class));
        // Optionnellement, vous pouvez vérifier que l'erreur est bien loggée si votre logger le permet.
    }


    // Cas où aucun ticket n'est trouvé pour le véhicule
    @Test
    public void processExitingVehicle_NoTicketFound_ShouldLogError() {
        try {
            lenient().when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("XYZ123");
            lenient().when(ticketDAO.getTicket(anyString())).thenReturn(null);

            parkingService.processExitingVehicle();

            verify(parkingSpotDAO, never()).updateParking(any(ParkingSpot.class));
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }
    }

    // Cas où updateTicket retourne false
    @Test
    public void processExitingVehicle_UpdateTicketFailed_ShouldLogError() {
        try {
            when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("XYZ123");
            when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(false);

            parkingService.processExitingVehicle();

            verify(parkingSpotDAO, never()).updateParking(any(ParkingSpot.class));
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }
    }

    // Cas où le type de véhicule est invalide
    @Test
    public void getNextParkingNumberIfAvailable_InvalidVehicleType_ShouldThrowException() {
        lenient().when(inputReaderUtil.readSelection()).thenReturn(3); // Invalid selection

        try {
            parkingService.getNextParkingNumberIfAvailable();
            fail("Exception should have been thrown for invalid vehicle type");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Entered input is invalid"));
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void getNextParkingNumberIfAvailable_NoAvailableParking_ShouldLogError() {
        try {
            lenient().when(inputReaderUtil.readSelection()).thenReturn(1); // CAR
            lenient().when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(-1);

            parkingService.getNextParkingNumberIfAvailable();
            fail("Exception should have been thrown due to no available parking");
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("Error fetching parking number from DB"));
        }
    }


    // Cas où le choix est invalide pour le type de véhicule
    @Test
    public void getVehicleType_InvalidInput_ShouldThrowException() {
        when(inputReaderUtil.readSelection()).thenReturn(3); // Invalid selection

        try {
            parkingService.getVehicleType();
            fail("Exception should have been thrown for invalid input");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("Entered input is invalid"));
        }
    }
}
