package com.eservice.ms_preference.service;

import com.eservice.ms_preference.model.Preference;
import org.javamoney.moneta.Money;
import org.springframework.stereotype.Service;

@Service
public class PreferenceService {

    private Preference preference;

    public PreferenceService() {
        // Initialisation avec des valeurs par défaut
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
}
