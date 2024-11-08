package com.parkit.parkingsystem.dao;


import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class TicketDAO {

    private static final Logger logger = LogManager.getLogger("TicketDAO");

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();
    private String expectedLicensePlate;

    public TicketDAO() {
    }

    public boolean saveTicket(Ticket ticket) {
        Connection con = null;
        PreparedStatement ps = null;
        boolean isSaved = false;

        try {
            if (ticket == null || ticket.getVehicleRegNumber() == null || ticket.getPrice() <= 0 || ticket.getInTime() == null) {
                logger.error("Invalid ticket object: {}", ticket);
                return false;
            }

            con = dataBaseConfig.getConnection();
            if (con == null) {
                logger.error("Failed to get a database connection");
                return false;
            }

            // Désactive l'auto-commit pour gérer la transaction manuellement
            con.setAutoCommit(false);

            // Logging SQL execution details
            logger.info("Executing SQL query: {}", DBConstants.SAVE_TICKET);
            logger.info("With parameters: Parking Spot ID = {}, Vehicle Reg Number = {}, Price = {}, In Time = {}, Out Time = {}",
                    ticket.getParkingSpot().getId(), ticket.getVehicleRegNumber(), ticket.getPrice(), ticket.getInTime(), ticket.getOutTime());

            ps = con.prepareStatement(DBConstants.SAVE_TICKET);
            ps.setInt(1, ticket.getParkingSpot().getId());
            ps.setString(2, ticket.getVehicleRegNumber());
            ps.setDouble(3, ticket.getPrice());
            ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
            ps.setTimestamp(5, (ticket.getOutTime() == null) ? null : new Timestamp(ticket.getOutTime().getTime()));

            int rowsAffected = ps.executeUpdate();
            logger.info("Rows affected by ticket save: {}", rowsAffected); // Debug log

            if (rowsAffected != 1) {
                logger.warn("Ticket save failed. Rows affected: {}", rowsAffected);
                con.rollback();  // Annule la transaction en cas d'échec
            } else {
                isSaved = true;
                logger.info("Ticket saved successfully.");
                con.commit();  // Confirme la transaction
            }
        } catch (SQLIntegrityConstraintViolationException ex) {
            logger.error("Integrity constraint violation: {}", ex.getMessage(), ex);
            try {
                if (con != null) con.rollback();  // Annule la transaction en cas de violation de contrainte
            } catch (SQLException e) {
                logger.error("Error during rollback", e);
            }
        } catch (SQLException ex) {
            logger.error("Error saving ticket: {}", ticket, ex);
            try {
                if (con != null) con.rollback();  // Annule la transaction en cas d'erreur SQL
            } catch (SQLException e) {
                logger.error("Error during rollback", e);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (con != null) con.setAutoCommit(true);  // Réactive l'auto-commit après la transaction
            } catch (SQLException e) {
                logger.error("Error resetting auto-commit", e);
            }
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }

        return isSaved;
    }



    public Ticket getTicket(String vehicleRegNumber) {
        Ticket ticket = null;

        if (vehicleRegNumber == null || vehicleRegNumber.isEmpty()) {
            logger.error("Invalid vehicle registration number: {}", vehicleRegNumber);
            return null;
        }

        try (Connection con = dataBaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement(DBConstants.GET_TICKET)) {

            ps.setString(1, vehicleRegNumber);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ticket = new Ticket();

                    String parkingTypeStr = rs.getString("TYPE");
                    ParkingSpot parkingSpot = null;
                    try {
                        if (parkingTypeStr != null) {
                            parkingSpot = new ParkingSpot(rs.getInt("PARKING_NUMBER"),
                                    ParkingType.valueOf(parkingTypeStr), false);
                        }
                    } catch (IllegalArgumentException ex) {
                        logger.error("Invalid parking type: {} for vehicle: {}", parkingTypeStr, vehicleRegNumber);
                    }

                    ticket.setParkingSpot(parkingSpot);
                    ticket.setId(rs.getInt("ID"));
                    ticket.setVehicleRegNumber(vehicleRegNumber);
                    ticket.setPrice(rs.getDouble("PRICE"));
                    ticket.setInTime(rs.getTimestamp("IN_TIME"));
                    ticket.setOutTime(rs.getTimestamp("OUT_TIME"));

                    logger.info("Ticket retrieved successfully for vehicle: {}", vehicleRegNumber);
                } else {
                    logger.info("No ticket found for vehicle registration number: {}", vehicleRegNumber);
                }
            }
        } catch (SQLException ex) {
            logger.error("SQL error fetching ticket for vehicle: {} ", vehicleRegNumber, ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return ticket;
    }

    public boolean updateTicket(Ticket ticket) {
        Connection con = null;
        try {
            con = dataBaseConfig.getConnection();
            PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
            ps.setDouble(1, ticket.getPrice());
            ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
            ps.setInt(3, ticket.getId());
            ps.execute();
            return true;
        } catch (Exception ex) {
            logger.error("Error saving ticket info", ex);
        } finally {
            dataBaseConfig.closeConnection(con);
        }
        return false;
    }

    public boolean getReduce(String vehicleRegNumber) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean isHere = false;

        try {
            con = dataBaseConfig.getConnection();
            ps = con.prepareStatement(DBConstants.GET_REDUCE);
            ps.setString(1, vehicleRegNumber);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException | ClassNotFoundException se) {
            logger.error("An error occurred while checking for reduction: ", se);
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception ignored) {}
            if (ps != null) try { ps.close(); } catch (Exception ignored) {}
            if (con != null) try { con.close(); } catch (Exception ignored) {}
        }
        return isHere;
    }

    public void setDataBaseConfig(DataBaseConfig dataBaseConfig) {
        this.dataBaseConfig = dataBaseConfig;
    }

    public Ticket getLatestTicket() {
        Ticket ticket = null;
        try (Connection con = dataBaseConfig.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM ticket ORDER BY ID DESC LIMIT 1")) {

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ticket = new Ticket();
                    ticket.setId(rs.getInt("ID"));
                    ticket.setParkingSpot(new ParkingSpot(rs.getInt("PARKING_NUMBER"), ParkingType.valueOf(rs.getString("TYPE")), false));
                    ticket.setVehicleRegNumber(rs.getString("VEHICLE_REG_NUMBER"));
                    ticket.setPrice(rs.getDouble("PRICE"));
                    ticket.setInTime(rs.getTimestamp("IN_TIME"));
                    ticket.setOutTime(rs.getTimestamp("OUT_TIME"));
                }
            }
        } catch (SQLException | ClassNotFoundException ex) {
            logger.error("SQL error while fetching the latest ticket", ex);
        }

        return ticket;
    }


    public Ticket getTicketByLicensePlate(String expectedLicensePlate) {
        this.expectedLicensePlate = expectedLicensePlate;
        return null;
    }

    public String getExpectedLicensePlate() {
        return expectedLicensePlate;
    }

    public void setExpectedLicensePlate(String expectedLicensePlate) {
        this.expectedLicensePlate = expectedLicensePlate;
    }
}
