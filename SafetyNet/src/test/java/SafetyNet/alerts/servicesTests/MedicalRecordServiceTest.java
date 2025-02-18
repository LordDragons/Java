package SafetyNet.alerts.servicesTests;

import SafetyNet.alerts.models.MedicalRecord;
import SafetyNet.alerts.models.Person;
import SafetyNet.alerts.repositorys.MedicalRecordRepositoryImpl;
import SafetyNet.alerts.services.MedicalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Utilisation de Mockito pour les tests
public class MedicalRecordServiceTest {

    @Mock
    private MedicalRecordRepositoryImpl medicalRecordRepository; // Ajout du mock manquant

    @InjectMocks
    private MedicalRecordService medicalRecordService; // Injecte les mocks dans le service

    private List<MedicalRecord> medicalRecords;
    private Person person;
    private MedicalRecord medicalRecord;

    @BeforeEach
    public void setUp() {
        medicalRecords = new ArrayList<>();
        person = new Person("John", "Doe", "123 Main St", "City", "12345", "555-5555", "john.doe@example.com");
        medicalRecord = new MedicalRecord("John", "Doe", "04/15/1990", new ArrayList<>(), new ArrayList<>());

        // Simule le comportement du repository
        when(medicalRecordRepository.findAll()).thenReturn(medicalRecords);
    }

    @Test
    public void testGetMedicalRecordForPerson() {
        // Arrange
        medicalRecords.add(medicalRecord);

        // Initialisation manuelle du cache
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
        // Arrange
        medicalRecords.add(medicalRecord);
        when(medicalRecordRepository.findAll()).thenReturn(medicalRecords);

        // Initialisation manuelle du cache avant l'appel de la méthode
        medicalRecordService.initializeCache();

        // Act
        MedicalRecord addedRecord = medicalRecordService.addMedicalRecord(medicalRecord);

        // Assert
        assertNotNull(addedRecord);
        assertTrue(medicalRecords.contains(addedRecord)); // Vérifie que le dossier médical a bien été ajouté
        verify(medicalRecordRepository, times(2)).findAll(); // Vérifie que findAll() est bien appelé
    }


    @Test
    public void testUpdateMedicalRecord() {
        // Arrange
        medicalRecords.add(medicalRecord);
        when(medicalRecordRepository.findByFirstNameAndLastName("John", "Doe"))
                .thenReturn(List.of(medicalRecord));

        // Initialisation manuelle du cache avant l'appel de la méthode
        medicalRecordService.initializeCache();

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
        when(medicalRecordRepository.findAll()).thenReturn(medicalRecords);

        // Initialisation manuelle du cache avant l'appel de la méthode
        medicalRecordService.initializeCache();

        // Act
        boolean result = medicalRecordService.deleteMedicalRecord("John", "Doe");

        // Assert
        assertTrue(result);
    }

    @Test
    public void testDeleteMedicalRecord_NotFound() {
        // Arrange
        when(medicalRecordRepository.findAll()).thenReturn(medicalRecords);


        // Act
        boolean result = medicalRecordService.deleteMedicalRecord("Non", "Existing");

        // Assert
        assertFalse(result);
    }
}
