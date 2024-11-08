package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TicketDAOTest {

    @InjectMocks
    private TicketDAO ticketDAO; // Système que nous testons

    @Mock
    private DataBaseConfig dataBaseConfig; // Mock de la configuration de la base de données

    @Mock
    private Connection con; // Mock de la connexion à la base de données

    @Mock
    private PreparedStatement ps; // Mock de la requête préparée

    @Mock
    private ResultSet rs; // Mock du résultat de la requête

    private Ticket ticket;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this); // Utilisez cette méthode si vous utilisez une version de Mockito antérieure à 3.4.0
        ticket = new Ticket();
        ticket.setParkingSpot(new ParkingSpot(1, ParkingType.CAR, true));
        ticket.setVehicleRegNumber("ABC123");
        ticket.setPrice(10.5);
        ticket.setInTime(new Date());
        ticket.setOutTime(new Date());
    }


    @Test
    public void testSaveTicket() throws SQLException, ClassNotFoundException {
        // Préparation de la simulation
        when(dataBaseConfig.getConnection()).thenReturn(con);
        when(con.prepareStatement(DBConstants.SAVE_TICKET)).thenReturn(ps);
        when(ps.executeUpdate()).thenReturn(1); // Simule une insertion réussie

        // Exécution de la méthode
        boolean result = ticketDAO.saveTicket(ticket);

        // Vérification des interactions et du résultat
        assertTrue(result, "Ticket should be saved successfully");
        verify(ps, times(1)).setInt(1, ticket.getParkingSpot().getId());
        verify(ps, times(1)).setString(2, ticket.getVehicleRegNumber());
        verify(ps, times(1)).setDouble(3, ticket.getPrice());
    }
    @Test
    void saveTicketCatchBlock() throws SQLException, ClassNotFoundException {
        when(dataBaseConfig.getConnection()).thenThrow(SQLException.class);

        boolean result = ticketDAO.saveTicket(null);

        assertFalse(result, "Error saving ticket");
    }


    @Test
    public void testGetTicket() throws SQLException, ClassNotFoundException {
        // Préparation de la simulation
        when(dataBaseConfig.getConnection()).thenReturn(con);
        when(con.prepareStatement(DBConstants.GET_TICKET)).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt("PARKING_NUMBER")).thenReturn(1);
        when(rs.getString("TYPE")).thenReturn("CAR");
        when(rs.getInt("ID")).thenReturn(1);
        when(rs.getString("VEHICLE_REG_NUMBER")).thenReturn("ABCPDF");
        when(rs.getDouble("PRICE")).thenReturn(10.5);
        when(rs.getTimestamp("IN_TIME")).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(rs.getTimestamp("OUT_TIME")).thenReturn(new Timestamp(System.currentTimeMillis()));

        // Exécution de la méthode
        Ticket result = ticketDAO.getTicket("ABCPDF");

        // Vérification du résultat
        assertNotNull(result, "Ticket should not be null");
        assertEquals("ABCPDF", result.getVehicleRegNumber(), "The vehicle registration number should match");
    }


    @Test
    void getTicketCatchBlock() throws SQLException, ClassNotFoundException {
        when(dataBaseConfig.getConnection()).thenThrow(SQLException.class);

        String vehicleRegNumber = "ABCPDF";
        Ticket result = ticketDAO.getTicket(vehicleRegNumber);

        assertNull(result,"is Null");
    }

    @Test
    public void testUpdateTicket() throws SQLException, ClassNotFoundException {
        // Préparation de la simulation
        when(dataBaseConfig.getConnection()).thenReturn(con);
        when(con.prepareStatement(DBConstants.UPDATE_TICKET)).thenReturn(ps);
        when(ps.execute()).thenReturn(true);

        // Exécution de la méthode
        boolean result = ticketDAO.updateTicket(ticket);

        // Vérification des interactions et du résultat
        assertTrue(result, "Ticket should be updated successfully");
        verify(ps, times(1)).setDouble(1, ticket.getPrice());
        verify(ps, times(1)).setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
    }

    @Test
    void updateTickerCatchBlock() throws SQLException, ClassNotFoundException {
        when(dataBaseConfig.getConnection()).thenThrow(SQLException.class);

        boolean result = ticketDAO.updateTicket(null);

        assertFalse(result);
    }

    @Test
    public void testGetReduce() throws SQLException, ClassNotFoundException {
        // Préparation de la simulation
        when(dataBaseConfig.getConnection()).thenReturn(con);
        when(con.prepareStatement(DBConstants.GET_REDUCE)).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(1); // Simule qu'une réduction est appliquée

        // Exécution de la méthode
        boolean result = ticketDAO.getReduce("ABCPDF");

        // Vérification du résultat
        assertTrue(result, "Vehicle should have a reduction applied");
    }

    @Test
    void testSaveTicket_NullTicket() throws SQLException, ClassNotFoundException {
        boolean result = ticketDAO.saveTicket(null);
        assertFalse(result, "Saving a null ticket should return false");
    }

    @Test
    void testSaveTicket_ConstraintViolation() throws SQLException, ClassNotFoundException {
        when(dataBaseConfig.getConnection()).thenReturn(con);
        when(con.prepareStatement(DBConstants.SAVE_TICKET)).thenReturn(ps);
        when(ps.executeUpdate()).thenThrow(SQLIntegrityConstraintViolationException.class);

        boolean result = ticketDAO.saveTicket(ticket);

        assertFalse(result, "Constraint violation should return false");
    }

    @Test
    void testGetTicket_InvalidVehicleRegNumber() {
        Ticket result = ticketDAO.getTicket(null);
        assertNull(result, "Null vehicle registration number should return null");

        result = ticketDAO.getTicket("");
        assertNull(result, "Empty vehicle registration number should return null");
    }

    @Test
    void testGetTicket_InvalidParkingType() throws SQLException, ClassNotFoundException {
        when(dataBaseConfig.getConnection()).thenReturn(con);
        when(con.prepareStatement(DBConstants.GET_TICKET)).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getString("TYPE")).thenReturn("INVALID_TYPE");

        Ticket result = ticketDAO.getTicket("ABCPDF");

        assertNotNull(result, "Ticket should not be null");
        assertNull(result.getParkingSpot(), "Parking spot should be null due to invalid type");
    }

    @Test
    void testGetTicket_NoTicketFound() throws SQLException, ClassNotFoundException {
        when(dataBaseConfig.getConnection()).thenReturn(con);
        when(con.prepareStatement(DBConstants.GET_TICKET)).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        Ticket result = ticketDAO.getTicket("NON_EXISTENT");

        assertNull(result, "No ticket should be found for non-existent vehicle registration number");
    }

    @Test
    void testUpdateTicket_NullOutTime() throws SQLException, ClassNotFoundException {
        ticket.setOutTime((Date) null);
        when(dataBaseConfig.getConnection()).thenReturn(con);
        when(con.prepareStatement(DBConstants.UPDATE_TICKET)).thenReturn(ps);

        boolean result = ticketDAO.updateTicket(ticket);

        assertFalse(result, "Updating ticket with null outTime should fail");
    }

    @Test
    void testGetReduce_NoReductionFound() throws SQLException, ClassNotFoundException {
        when(dataBaseConfig.getConnection()).thenReturn(con);
        when(con.prepareStatement(DBConstants.GET_REDUCE)).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);

        boolean result = ticketDAO.getReduce("NO_REDUCE");

        assertFalse(result, "No reduction should be applied if record is not found");
    }

    @Test
    void testGetLatestTicket() {
        Ticket result = ticketDAO.getLatestTicket();
        assertNull(result, "getLatestTicket should return null");
    }

    @Test
    void testGetTicketByLicensePlate() {
        Ticket result = ticketDAO.getTicketByLicensePlate("XYZ123");
        assertNull(result, "getTicketByLicensePlate should return null");
    }



}
