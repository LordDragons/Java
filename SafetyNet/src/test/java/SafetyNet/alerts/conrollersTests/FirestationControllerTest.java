package SafetyNet.alerts.conrollersTests;

import SafetyNet.alerts.controllers.FirestationController;
import SafetyNet.alerts.dto.FirestationDTO;
import SafetyNet.alerts.dto.HouseDTO;
import SafetyNet.alerts.models.Data;
import SafetyNet.alerts.models.Firestation;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FirestationControllerTest {

    @Mock
    private FirestationService firestationService;

    @Mock
    private MedicalRecordService medicalRecordService;

    @Mock
    private Data data;

    @InjectMocks
    private FirestationController firestationController;

    @BeforeEach
    void setUp() {
        // Initialize mocks before each test
    }

    @Test
    void testGetPhoneAlert() {
        int stationNumber = 1;
        List<String> phoneNumbers = Arrays.asList("123456789", "987654321");

        // Mock the service layer
        when(firestationService.getPhonesByFirestation(stationNumber)).thenReturn(phoneNumbers);

        ResponseEntity<List<String>> response = firestationController.getPhoneAlert(stationNumber);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(phoneNumbers, response.getBody());
    }

    @Test
    void testGetPhoneAlert_noContent() {
        int stationNumber = 1;
        when(firestationService.getPhonesByFirestation(stationNumber)).thenReturn(List.of());

        ResponseEntity<List<String>> response = firestationController.getPhoneAlert(stationNumber);

        assertEquals(204, response.getStatusCodeValue()); // No Content
    }

    @Test
    void testGetHouseholdsByStations() {
        List<Integer> stations = Arrays.asList(1, 2);
        List<HouseDTO> households = Arrays.asList(
                new HouseDTO("address1", Arrays.asList()),
                new HouseDTO("address2", Arrays.asList())
        );

        when(firestationService.getHouseholdsByStations(stations)).thenReturn(households);

        ResponseEntity<List<HouseDTO>> response = firestationController.getHouseholdsByStations(stations);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(households, response.getBody());
    }


    @Test
    void testGetFireDetails() {
        String address = "123 Main St";
        FirestationDTO firestationDTO = new FirestationDTO();

        when(firestationService.getFireDetails(address)).thenReturn(firestationDTO);

        ResponseEntity<FirestationDTO> response = firestationController.getFireDetails(address);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetFireDetails_notFound() {
        String address = "Nonexistent Address";
        when(firestationService.getFireDetails(address)).thenReturn(null);

        ResponseEntity<FirestationDTO> response = firestationController.getFireDetails(address);

        assertEquals(404, response.getStatusCodeValue()); // Not Found
    }

    @Test
    void testGetPersonsCoveredByStationWithNoResidents() {
        int stationNumber = 1;

        // Create an empty list of residents
        List<FirestationDTO.ResidentInfo> residents = Collections.emptyList();

        FirestationDTO firestationDTO = new FirestationDTO("123 Main St", stationNumber, 0L, 0L, residents);

        when(firestationService.getPersonsCoveredByStation(stationNumber)).thenReturn(firestationDTO);

        ResponseEntity<FirestationDTO> response = firestationController.getPersonsCoveredByStation(stationNumber);

        assertEquals(204, response.getStatusCodeValue());  // Expect 204 if no residents
        assertNull(response.getBody());  // No body in the response
    }


    @Test
    void testGetPersonsCoveredByStation() {
        int stationNumber = 1;
        List<FirestationDTO.ResidentInfo> residents = Arrays.asList(
                new FirestationDTO.ResidentInfo("John Doe", "123456789", 30, List.of("Aspirin"), List.of("Peanuts")),
                new FirestationDTO.ResidentInfo("Jane Smith", "987654321", 40, List.of("Ibuprofen"), List.of("None"))
        );

        FirestationDTO firestationDTO = new FirestationDTO("123 Main St", stationNumber, 2L, 0L, residents);

        when(firestationService.getPersonsCoveredByStation(stationNumber)).thenReturn(firestationDTO);

        ResponseEntity<FirestationDTO> response = firestationController.getPersonsCoveredByStation(stationNumber);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().getResidents().isEmpty());
    }



    @Test
    void testAddFirestation() {
        Firestation firestation = new Firestation();
        Firestation createdFirestation = new Firestation();

        when(firestationService.addFirestation(firestation)).thenReturn(createdFirestation);

        ResponseEntity<Firestation> response = firestationController.addFirestation(firestation);

        assertEquals(201, response.getStatusCodeValue()); // Created
        assertNotNull(response.getBody());
        assertEquals(createdFirestation, response.getBody());
    }

    @Test
    void testUpdateFirestation() {
        String address = "123 Main St";
        int station = 1;
        Firestation updatedFirestation = new Firestation();

        when(firestationService.updateFirestation(address, station)).thenReturn(updatedFirestation);

        ResponseEntity<Firestation> response = firestationController.updateFirestation(address, station);

        assertEquals(200, response.getStatusCodeValue()); // OK
        assertNotNull(response.getBody());
        assertEquals(updatedFirestation, response.getBody());
    }

    @Test
    void testUpdateFirestation_notFound() {
        String address = "Nonexistent Address";
        int station = 1;

        when(firestationService.updateFirestation(address, station)).thenReturn(null);

        ResponseEntity<Firestation> response = firestationController.updateFirestation(address, station);

        assertEquals(404, response.getStatusCodeValue()); // Not Found
    }

    @Test
    void testDeleteFirestation() {
        String address = "123 Main St";
        when(firestationService.deleteFirestation(address)).thenReturn(true);

        ResponseEntity<Void> response = firestationController.deleteFirestation(address);

        assertEquals(204, response.getStatusCodeValue()); // No Content
    }

    @Test
    void testDeleteFirestation_notFound() {
        String address = "Nonexistent Address";
        when(firestationService.deleteFirestation(address)).thenReturn(false);

        ResponseEntity<Void> response = firestationController.deleteFirestation(address);

        assertEquals(404, response.getStatusCodeValue()); // Not Found
    }
}

