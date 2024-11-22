package SafetyNet.alerts.conrollersTests;


import SafetyNet.alerts.controllers.MedicalRecordController;
import SafetyNet.alerts.dto.HouseDTO;
import SafetyNet.alerts.models.MedicalRecord;
import SafetyNet.alerts.services.FirestationService;
import SafetyNet.alerts.services.MedicalRecordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordControllerTest {

    @Mock
    private FirestationService firestationService;

    @Mock
    private MedicalRecordService medicalRecordService;

    @InjectMocks
    private MedicalRecordController medicalRecordController;

    @BeforeEach
    void setUp() {
        // Initialize mocks before each test
    }

    @Test
    void testGetFloodStations() {
        List<Integer> stations = Arrays.asList(1, 2);
        List<HouseDTO> households = Arrays.asList(
                new HouseDTO("address1", Arrays.asList()),
                new HouseDTO("address2", Arrays.asList())
        );

        when(firestationService.getHouseholdsByStations(stations)).thenReturn(households);

        ResponseEntity<List<HouseDTO>> response = medicalRecordController.getFloodStations(stations);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(households, response.getBody());
    }

    @Test
    void testGetFloodStations_noContent() {
        List<Integer> stations = Arrays.asList(1, 2);
        when(firestationService.getHouseholdsByStations(stations)).thenReturn(List.of());

        ResponseEntity<List<HouseDTO>> response = medicalRecordController.getFloodStations(stations);

        assertEquals(204, response.getStatusCodeValue());  // No Content
    }

    @Test
    void testAddMedicalRecord() {
        MedicalRecord medicalRecord = new MedicalRecord();
        MedicalRecord createdRecord = new MedicalRecord();

        when(medicalRecordService.addMedicalRecord(medicalRecord)).thenReturn(createdRecord);

        ResponseEntity<MedicalRecord> response = medicalRecordController.addMedicalRecord(medicalRecord);

        assertEquals(201, response.getStatusCodeValue());  // Created
        assertNotNull(response.getBody());
        assertEquals(createdRecord, response.getBody());
    }

    @Test
    void testUpdateMedicalRecord() {
        MedicalRecord medicalRecord = new MedicalRecord();
        MedicalRecord updatedRecord = new MedicalRecord();

        when(medicalRecordService.updateMedicalRecord(medicalRecord)).thenReturn(updatedRecord);

        ResponseEntity<MedicalRecord> response = medicalRecordController.updateMedicalRecord(medicalRecord);

        assertEquals(200, response.getStatusCodeValue());  // OK
        assertNotNull(response.getBody());
        assertEquals(updatedRecord, response.getBody());
    }

    @Test
    void testUpdateMedicalRecord_notFound() {
        MedicalRecord medicalRecord = new MedicalRecord();

        when(medicalRecordService.updateMedicalRecord(medicalRecord)).thenReturn(null);

        ResponseEntity<MedicalRecord> response = medicalRecordController.updateMedicalRecord(medicalRecord);

        assertEquals(404, response.getStatusCodeValue());  // Not Found
    }

    @Test
    void testDeleteMedicalRecord() {
        String firstName = "John";
        String lastName = "Doe";

        when(medicalRecordService.deleteMedicalRecord(firstName, lastName)).thenReturn(true);

        ResponseEntity<Void> response = medicalRecordController.deleteMedicalRecord(firstName, lastName);

        assertEquals(204, response.getStatusCodeValue());  // No Content
    }

    @Test
    void testDeleteMedicalRecord_notFound() {
        String firstName = "John";
        String lastName = "Doe";

        when(medicalRecordService.deleteMedicalRecord(firstName, lastName)).thenReturn(false);

        ResponseEntity<Void> response = medicalRecordController.deleteMedicalRecord(firstName, lastName);

        assertEquals(404, response.getStatusCodeValue());  // Not Found
    }
}

