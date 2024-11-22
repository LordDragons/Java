package SafetyNet.alerts.servicesTests;


import SafetyNet.alerts.models.Data;
import SafetyNet.alerts.models.MedicalRecord;
import SafetyNet.alerts.models.Person;
import SafetyNet.alerts.services.MedicalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MedicalRecordServiceTest {

    @Mock
    private Data data;

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    private List<MedicalRecord> medicalRecords;
    private Person person;
    private MedicalRecord medicalRecord;

    @BeforeEach
    public void setUp() {
        medicalRecords = new ArrayList<>();
        person = new Person("John", "Doe", "123 Main St", "City", "12345", "555-5555", "john.doe@example.com");
        medicalRecord = new MedicalRecord("John", "Doe", "04/15/1990", new ArrayList<>(), new ArrayList<>());

        // Mock the Data object to return the list of medical records
        when(data.getMedicalrecords()).thenReturn(medicalRecords);
    }

    @Test
    public void testGetMedicalRecordForPerson() {
        // Arrange
        medicalRecords.add(medicalRecord);

        // Manually initialize the cache
        medicalRecordService.initializeCache();

        // Act
        MedicalRecord result = medicalRecordService.getMedicalRecordForPerson(person);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
    }


    @Test
    public void testAddMedicalRecord() {
        // Act
        MedicalRecord addedRecord = medicalRecordService.addMedicalRecord(medicalRecord);

        // Assert
        assertNotNull(addedRecord);
        assertTrue(medicalRecords.contains(addedRecord));
    }

    @Test
    public void testUpdateMedicalRecord() {
        // Arrange
        medicalRecords.add(medicalRecord);

        // Act
        MedicalRecord updatedRecord = new MedicalRecord("John", "Doe", "04/15/1990", List.of("Aspirin"), List.of("Peanuts"));
        MedicalRecord result = medicalRecordService.updateMedicalRecord(updatedRecord);

        // Assert
        assertNotNull(result);
        assertEquals(updatedRecord.getMedications(), result.getMedications());
        assertEquals(updatedRecord.getAllergies(), result.getAllergies());
    }

    @Test
    public void testDeleteMedicalRecord() {
        // Arrange
        medicalRecords.add(medicalRecord);

        // Act
        boolean result = medicalRecordService.deleteMedicalRecord("John", "Doe");

        // Assert
        assertTrue(result);
        assertFalse(medicalRecords.contains(medicalRecord));
    }

    @Test
    public void testDeleteMedicalRecord_NotFound() {
        // Act
        boolean result = medicalRecordService.deleteMedicalRecord("Non", "Existing");

        // Assert
        assertFalse(result);
    }
}
