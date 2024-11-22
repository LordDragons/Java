package SafetyNet.alerts.modelsTests;

import SafetyNet.alerts.models.Firestation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FirestationTest {

    @Test
    public void testFirestationConstructorAndGetters() {
        // Arrange
        String expectedAddress = "123 Main St";
        Integer expectedStation = 5;

        // Act
        Firestation firestation = new Firestation(expectedAddress, expectedStation);

        // Assert
        assertNotNull(firestation);
        assertEquals(expectedAddress, firestation.getAddress());
        assertEquals(expectedStation, firestation.getStation());
    }

    @Test
    public void testSetters() {
        // Arrange
        Firestation firestation = new Firestation();

        // Act
        firestation.setAddress("456 Oak St");
        firestation.setStation(10);

        // Assert
        assertEquals("456 Oak St", firestation.getAddress());
        assertEquals(10, firestation.getStation());
    }

    @Test
    public void testToString() {
        // Arrange
        Firestation firestation = new Firestation("789 Pine St", 3);

        // Act
        String result = firestation.toString();

        // Assert
        assertEquals("firestation{address=789 Pine Ststation=3}", result);
    }
}

