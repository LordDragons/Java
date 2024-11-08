package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.Ticket;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

public class FareCalculatorService {

    private final TicketDAO ticketDAO = new TicketDAO(); // Instance de TicketDAO

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect: " + ticket.getOutTime());
        }

        double durationInHours = calculateDurationInHours(ticket.getInTime(), ticket.getOutTime());

        // Appliquer une période gratuite de 30 minutes
        if (durationInHours <= 0.5) {
            ticket.setPrice(0);
        } else {
            BigDecimal price;
            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR:
                    price = BigDecimal.valueOf((durationInHours - 0.5) * Fare.CAR_RATE_PER_HOUR);
                    break;
                case BIKE:
                    price = BigDecimal.valueOf((durationInHours - 0.5) * Fare.BIKE_RATE_PER_HOUR);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
            }

            // Vérifier si le véhicule est récurrent pour appliquer une réduction de 5 %
            if (ticketDAO.getReduce(ticket.getVehicleRegNumber())) {
                price = price.multiply(BigDecimal.valueOf(0.95)); // Appliquer une réduction de 5 %
            }

            // Fixer le prix avec deux décimales
            ticket.setPrice(price.setScale(2, RoundingMode.HALF_UP).doubleValue());
        }
    }

    private double calculateDurationInHours(Date inTime, Date outTime) {
        long durationInMillis = outTime.getTime() - inTime.getTime();
        return (double) durationInMillis / (1000 * 60 * 60);
    }
}
