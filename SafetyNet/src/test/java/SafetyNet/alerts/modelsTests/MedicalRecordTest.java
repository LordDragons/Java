package SafetyNet.alerts.modelsTests;


import SafetyNet.alerts.models.MedicalRecord;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MedicalRecordTest {

    @Test
    public void testDefaultConstructor() {
        // Arrange & Act
        MedicalRecord medicalRecord = new MedicalRecord();

        // Assert
        assertNotNull(medicalRecord);
        assertNull(medicalRecord.getFirstName());
        assertNull(medicalRecord.getLastName());
        assertNull(medicalRecord.getBirthDate());
        assertNull(medicalRecord.getMedications());
        assertNull(medicalRecord.getAllergies());
    }

    @Test
    public void testParameterizedConstructor() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String birthdate = "01/01/1990";
        List<String> medications = Arrays.asList("Med1", "Med2");
        List<String> allergies = Arrays.asList("Pollen", "Peanuts");

        // Act
        MedicalRecord medicalRecord = new MedicalRecord(firstName, lastName, birthdate, medications, allergies);

        // Assert
        assertEquals(firstName, medicalRecord.getFirstName());
        assertEquals(lastName, medicalRecord.getLastName());
        assertEquals(birthdate, medicalRecord.getBirthDate());
        assertEquals(medications, medicalRecord.getMedications());
        assertEquals(allergies, medicalRecord.getAllergies());
    }

    @Test
    public void testSettersAndGetters() {
        // Arrange
        MedicalRecord medicalRecord = new MedicalRecord();
        String firstName = "Jane";
        String lastName = "Smith";
        String birthdate = "02/14/1985";
        List<String> medications = Collections.singletonList("Med3");
        List<String> allergies = Collections.singletonList("Dust");

        // Act
        medicalRecord.setFirstName(firstName);
        medicalRecord.setLastName(lastName);
        medicalRecord.setBirthDate(birthdate);
        medicalRecord.setMedications(medications);
        medicalRecord.setAllergies(allergies);

        // Assert
        assertEquals(firstName, medicalRecord.getFirstName());
        assertEquals(lastName, medicalRecord.getLastName());
        assertEquals(birthdate, medicalRecord.getBirthDate());
        assertEquals(medications, medicalRecord.getMedications());
        assertEquals(allergies, medicalRecord.getAllergies());
    }

    @Test
    public void testToString() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String birthdate = "01/01/1990";
        List<String> medications = Arrays.asList("Med1", "Med2");
        List<String> allergies = Arrays.asList("Pollen", "Peanuts");

        MedicalRecord medicalRecord = new MedicalRecord(firstName, lastName, birthdate, medications, allergies);

        // Act
        String result = medicalRecord.toString();

        // Assert
        String expected = "MedicalRecord{firstName='John', lastName='Doe', birthdate='01/01/1990', medications=[Med1, Med2], allergies=[Pollen, Peanuts]}";
        assertEquals(expected, result);
    }
}

