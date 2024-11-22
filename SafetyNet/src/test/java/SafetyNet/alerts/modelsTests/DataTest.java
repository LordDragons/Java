package SafetyNet.alerts.modelsTests;

import SafetyNet.alerts.models.Data;
import SafetyNet.alerts.models.Firestation;
import SafetyNet.alerts.models.MedicalRecord;
import SafetyNet.alerts.models.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

public class DataTest {

    private Data data;

    @BeforeEach
    public void setUp() {
        data = new Data();
    }

    @Test
    public void testSetAndGetPersons() {
        // Arrange
        Person person1 = new Person();
        person1.setFirstName("John");
        person1.setLastName("Doe");

        Person person2 = new Person();
        person2.setFirstName("Jane");
        person2.setLastName("Doe");

        // Set the persons in the Data object
        data.setPersons(Arrays.asList(person1, person2));

        // Act
        List<Person> persons = data.getPersons();

        // Assert
        assertNotNull(persons);
        assertEquals(2, persons.size());
        assertEquals("John", persons.get(0).getFirstName());
        assertEquals("Jane", persons.get(1).getFirstName());
    }


    @Test
    public void testSetAndGetFirestations() {
        // Arrange
        Firestation firestation1 = new Firestation("123 Main St", 1);
        Firestation firestation2 = new Firestation("456 Oak St", 2);
        data.setFirestations(Arrays.asList(firestation1, firestation2));

        // Act
        List<Firestation> firestations = data.getFirestations();

        // Assert
        assertNotNull(firestations);
        assertEquals(2, firestations.size());
        assertEquals("123 Main St", firestations.get(0).getAddress());
        assertEquals(2, firestations.get(1).getStation());
    }

    @Test
    public void testSetAndGetMedicalRecords() {
        // Arrange
        MedicalRecord medicalRecord1 = new MedicalRecord("John", "Doe", "01/01/1990",
                Arrays.asList("Med1"), Arrays.asList("Allergy1"));
        MedicalRecord medicalRecord2 = new MedicalRecord("Jane", "Smith", "02/02/1992",
                Arrays.asList("Med2"), Arrays.asList("Allergy2"));
        data.setMedicalrecords(Arrays.asList(medicalRecord1, medicalRecord2));

        // Act
        List<MedicalRecord> medicalRecords = data.getMedicalrecords();

        // Assert
        assertNotNull(medicalRecords);
        assertEquals(2, medicalRecords.size());
        assertEquals("John", medicalRecords.get(0).getFirstName());  // Check firstName for medicalRecord1
        assertEquals("Med2", medicalRecords.get(1).getMedications().get(0)); // Check medications for medicalRecord2
    }
}

