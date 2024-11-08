package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class TicketTest {

    private Ticket ticket;
    private ParkingSpot parkingSpot;

    @BeforeEach
    public void setUp() {
        ticket = new Ticket();
        parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);
    }

    @Test
    public void testSetAndGetId() {
        ticket.setId(1);
        assertEquals(1, ticket.getId(), "Ticket ID should be 1");
    }

    @Test
    public void testSetAndGetParkingSpot() {
        ticket.setParkingSpot(parkingSpot);
        assertEquals(parkingSpot, ticket.getParkingSpot(), "Parking spot should be the same as set");
    }

    @Test
    public void testSetAndGetVehicleRegNumber() {
        ticket.setVehicleRegNumber("ABC123");
        assertEquals("ABC123", ticket.getVehicleRegNumber(), "Vehicle registration number should be ABC123");
    }

    @Test
    public void testSetAndGetPrice() {
        ticket.setPrice(10.5);
        assertEquals(10.5, ticket.getPrice(), "Price should be 10.5");
    }

    @Test
    public void testSetAndGetInTime() {
        Date inTime = new Date();
        ticket.setInTime(inTime);
        assertEquals(inTime, ticket.getInTime(), "In-time should be the same as set");
    }

    @Test
    public void testSetAndGetOutTime() {
        Date outTime = new Date();
        ticket.setOutTime(outTime);
        assertEquals(outTime, ticket.getOutTime(), "Out-time should be the same as set");
    }

    @Test
    public void testSetOutTimeWithLocalDateTime() {
        LocalDateTime now = LocalDateTime.now();
        ticket.setOutTime(now);
        // Verify that outTime is not null and is correctly set to a Timestamp value
        assertNotNull(ticket.getOutTime(), "Out-time should not be null after setting with LocalDateTime");
        assertTrue(ticket.getOutTime() instanceof Timestamp, "Out-time should be an instance of Timestamp after setting with LocalDateTime");
    }

    @Test
    public void testGetLicensePlate() {
        assertEquals("", ticket.getLicensePlate(), "License plate should return an empty string");
    }

    @Test
    public void testGetParkingSpotId() {
        assertNull(ticket.getParkingSpotId(), "ParkingSpotId should return null");
    }

    @Test
    public void testGetExitTime() {
        assertNull(ticket.getExitTime(), "ExitTime should return null");
    }
}
