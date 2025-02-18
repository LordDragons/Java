package com.eservice.ms_preference.service;

import com.eservice.ms_preference.model.Preference;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;
import tripPricer.TripPricer;
import tripPricer.Provider;

import java.util.List;

@Service
public class PreferenceService {

    private Preference preference;
    private TripPricer tripPricer;

    public PreferenceService(TripPricer tripPricer) {
        this.tripPricer = tripPricer;
        this.preference = new Preference();
    }

    public PreferenceService() {
        this.preference = new Preference();
    }

    public Preference getPreferences() {
        return preference;
    }

    public void updatePreferences(Preference newPreferences) {
        if (newPreferences != null) {
            this.preference = newPreferences;
        }
    }

    public void setAttractionProximity(int attractionProximity) {
        preference.setAttractionProximity(attractionProximity);
    }

    public void setPriceRange(Money lowerPricePoint, Money highPricePoint) {
        preference.setLowerPricePoint(lowerPricePoint);
        preference.setHighPricePoint(highPricePoint);
    }

    public void setTripDetails(int tripDuration, int ticketQuantity) {
        preference.setTripDuration(tripDuration);
        preference.setTicketQuantity(ticketQuantity);
    }

    public void setNumberOfPeople(int numberOfAdults, int numberOfChildren) {
        preference.setNumberOfAdults(numberOfAdults);
        preference.setNumberOfChildren(numberOfChildren);
    }

//        public List<Provider> Preference(int numberOfAdults, int numberOfChildren, int tripDuration) {
//            int cumulativeRewardPoints = user.getUserRewards().stream().mapToInt(reward -> reward.getPoints()).sum();
//        String tripPricerApiKey = "test-server-api-key";
//        return tripPricer.getPrice(tripPricerApiKey, user.getUserId(), numberOfAdults, numberOfChildren, tripDuration, cumulativeRewardPoints);
//    }
}
