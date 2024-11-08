package com.parkit.parkingsystem.model;

import com.parkit.parkingsystem.constants.ParkingType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParkingSpotTest {

    @Test
    void testParkingSpotCreation() {
        // Test de la création d'un parking
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);

        assertEquals(1, parkingSpot.getId()); // Vérification de l'ID
        assertEquals(ParkingType.CAR, parkingSpot.getParkingType()); // Vérification du type de parking
        assertTrue(parkingSpot.isAvailable()); // Vérification de la disponibilité
    }

    @Test
    void testSettersAndGetters() {
        // Test des setters et getters
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);

        parkingSpot.setId(2);
        parkingSpot.setParkingType(ParkingType.BIKE);
        parkingSpot.setAvailable(false);

        assertEquals(2, parkingSpot.getId()); // Vérification de l'ID après changement
        assertEquals(ParkingType.BIKE, parkingSpot.getParkingType()); // Vérification du type de parking après changement
        assertFalse(parkingSpot.isAvailable()); // Vérification de la disponibilité après changement
    }

    @Test
    void testEqualsAndHashCode() {
        // Test des méthodes equals et hashCode
        ParkingSpot parkingSpot1 = new ParkingSpot(1, ParkingType.CAR, true);
        ParkingSpot parkingSpot2 = new ParkingSpot(1, ParkingType.BIKE, false);
        ParkingSpot parkingSpot3 = new ParkingSpot(2, ParkingType.CAR, true);

        // Test de l'égalité
        assertEquals(parkingSpot1, parkingSpot2); // Même ID donc égaux
        assertNotEquals(parkingSpot1, parkingSpot3); // ID différents donc inégaux

        // Test de hashCode
        assertEquals(parkingSpot1.hashCode(), parkingSpot2.hashCode()); // Même ID donc hashCode identiques
        assertNotEquals(parkingSpot1.hashCode(), parkingSpot3.hashCode()); // ID différents donc hashCode différents
    }

    @Test
    void testAvailabilityToggle() {
        // Test de la modification de la disponibilité
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, true);

        assertTrue(parkingSpot.isAvailable()); // Initialement disponible

        parkingSpot.setAvailable(false);
        assertFalse(parkingSpot.isAvailable()); // Après modification, il doit être indisponible

        parkingSpot.setAvailable(true);
        assertTrue(parkingSpot.isAvailable()); // Après modification, il doit être à nouveau disponible
    }

    @Test
    void testParkingSpotWithNullType() {
        // Test avec un ParkingSpot avec un type nul (si applicable)
        ParkingSpot parkingSpot = new ParkingSpot(1, null, true);

        assertEquals(1, parkingSpot.getId()); // Vérification de l'ID
        assertNull(parkingSpot.getParkingType()); // Type de parking nul
        assertTrue(parkingSpot.isAvailable()); // La disponibilité est encore valide
    }
}
