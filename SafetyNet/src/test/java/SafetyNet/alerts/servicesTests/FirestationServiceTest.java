package SafetyNet.alerts.servicesTests;


import SafetyNet.alerts.dto.FirestationDTO;
import SafetyNet.alerts.dto.HouseDTO;
import SafetyNet.alerts.models.Data;
import SafetyNet.alerts.models.Firestation;
import SafetyNet.alerts.models.MedicalRecord;
import SafetyNet.alerts.models.Person;
import SafetyNet.alerts.repositorys.FirestationRepository;
import SafetyNet.alerts.repositorys.PersonRepository;
import SafetyNet.alerts.services.FirestationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class FirestationServiceTest {

    @Mock
    private FirestationRepository firestationRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private Data data;

    @InjectMocks
    private FirestationService firestationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPhonesByFirestation() {
        // Arrange
        int station = 1;
        List<String> addresses = Arrays.asList("123 Main St", "456 Elm St");
        List<String> phones = Arrays.asList("123-456-7890", "987-654-3210");

        when(firestationRepository.findAddressesByStationNumber(station)).thenReturn(addresses);
        when(personRepository.findPhonesByAddresses(addresses)).thenReturn(phones);

        // Act
        List<String> result = firestationService.getPhonesByFirestation(station);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("123-456-7890"));
        assertTrue(result.contains("987-654-3210"));
    }

    @Test
    public void testGetHouseholdsByStations() {
        // Arrange
        List<Integer> stations = Arrays.asList(1, 2);
        List<String> addresses = Arrays.asList("123 Main St", "456 Elm St");
        List<HouseDTO.Resident> residents = new ArrayList<>();
        HouseDTO houseDTO = new HouseDTO("123 Main St", residents);

        when(firestationRepository.findAddressesByStationNumber(1)).thenReturn(Arrays.asList("123 Main St"));
        when(firestationRepository.findAddressesByStationNumber(2)).thenReturn(Arrays.asList("456 Elm St"));
        when(personRepository.findByAddress(Arrays.asList("123 Main St"))).thenReturn(new ArrayList<>());
        when(personRepository.findByAddress(Arrays.asList("456 Elm St"))).thenReturn(new ArrayList<>());

        // Act
        List<HouseDTO> result = firestationService.getHouseholdsByStations(stations);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("123 Main St", result.get(0).getAddress());
        assertEquals("456 Elm St", result.get(1).getAddress());
    }

    @Test
    public void testCreateResidentFromPerson() {
        // Arrange: Mock the data
        Person person = new Person("John", "Doe", "123 Main St", "SomeCity", "12345", "555-5555", "john.doe@example.com");

        // Create a mock medical record with LocalDate format for birthDate
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "04/15/1990", new ArrayList<>(), new ArrayList<>());
        when(data.getMedicalrecords()).thenReturn(Arrays.asList(medicalRecord));

        // Act: Call the method to create a resident from person
        HouseDTO.Resident resident = firestationService.createResidentFromPerson(person);

        // Assert: Verify the result
        assertNotNull(resident);
        assertEquals("John", resident.getFirstName());
        assertEquals("Doe", resident.getLastName());
        assertEquals("555-5555", resident.getPhone());
        assertEquals(34, resident.getAge());  // John should be 34 years old (as of 2024)
        assertTrue(resident.getMedications().isEmpty());
        assertTrue(resident.getAllergies().isEmpty());
    }


    @Test
    public void testCreateResidentFromPersonWithMissingMedicalRecord() {
        // Arrange: Mock the data
        Person person = new Person("John", "Doe", "123 Main St", "SomeCity", "12345", "555-5555", "john.doe@example.com");

        // Mock an empty list of MedicalRecords or a list without a matching MedicalRecord
        MedicalRecord medicalRecord = new MedicalRecord("Jane", "Smith", "04/15/1990", new ArrayList<>(), new ArrayList<>()); // Different name
        when(data.getMedicalrecords()).thenReturn(Arrays.asList(medicalRecord)); // No matching record for John Doe

        // Spy on the service to verify method calls
        FirestationService spyService = spy(firestationService);

        // Act: Call the method to create a resident from person
        HouseDTO.Resident resident = spyService.createResidentFromPerson(person);

        // Assert: Verify the result is null
        assertNull(resident);

        // Verify that logMissingMedicalRecord() was called
        verify(spyService, times(1)).logMissingMedicalRecord(person);
    }



    @Test
    public void testGetPersonsCoveredByStation() {
        // Arrange
        int stationNumber = 1;
        List<String> addresses = Arrays.asList("123 Main St");

        // Create a person with valid firstName and lastName to match the MedicalRecord
        Person person = new Person("John", "Doe", "123 Main St", "SomeCity", "12345", "555-5555", "john.doe@example.com");
        List<Person> persons = Arrays.asList(person);

        // Create a MedicalRecord with matching firstName and lastName
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "04/15/1990", new ArrayList<>(), new ArrayList<>());

        // Mock repository calls
        when(firestationRepository.findAddressesByStationNumber(stationNumber)).thenReturn(addresses);
        when(personRepository.findByAddress(addresses)).thenReturn(persons);
        when(data.getMedicalrecords()).thenReturn(Arrays.asList(medicalRecord));

        // Act
        FirestationDTO result = firestationService.getPersonsCoveredByStation(stationNumber);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getResidents().size());

        // Verify the correct resident information is returned
        FirestationDTO.ResidentInfo residentInfo = result.getResidents().get(0);
        assertEquals("John Doe", residentInfo.getName());
    }

    @Test
    public void testGetFireDetails() {
        // Arrange: Mock the data
        String address = "123 Main St";

        // Create mock persons
        Person person1 = new Person("John", "Doe", address, "SomeCity", "12345", "555-5555", "john.doe@example.com");
        Person person2 = new Person("Jane", "Smith", address, "SomeCity", "12345", "555-5555", "jane.smith@example.com");
        List<Person> persons = Arrays.asList(person1, person2);

        // Create mock firestation
        Firestation firestation = new Firestation(address, 1);

        // Create mock medical records
        MedicalRecord medicalRecord1 = new MedicalRecord("John", "Doe", "04/15/1990", new ArrayList<>(), new ArrayList<>());
        MedicalRecord medicalRecord2 = new MedicalRecord("Jane", "Smith", "06/20/2005", new ArrayList<>(), new ArrayList<>());
        List<MedicalRecord> medicalRecords = Arrays.asList(medicalRecord1, medicalRecord2);

        // Mock methods of Data class
        when(data.getPersons()).thenReturn(persons);
        when(data.getFirestations()).thenReturn(Arrays.asList(firestation));
        when(data.getMedicalrecords()).thenReturn(medicalRecords);

        // Act: Call the method
        FirestationDTO result = firestationService.getFireDetails(address);

        // Assert: Validate the returned FirestationDTO
        assertNotNull(result);
        assertEquals(address, result.getAddress());
        assertEquals(1, result.getStation());
        assertEquals(2, result.getNumberOfAdults());
        assertEquals(0, result.getNumberOfChildren());
        assertEquals(2, result.getResidents().size());

        // Validate resident details
        FirestationDTO.ResidentInfo resident1 = result.getResidents().get(0);
        assertEquals("John Doe", resident1.getName());
        assertEquals(34, resident1.getAge());  // John should be 34 years old (as of 2024)

        FirestationDTO.ResidentInfo resident2 = result.getResidents().get(1);
        assertEquals("Jane Smith", resident2.getName());
        assertEquals(19, resident2.getAge());  // Jane should be 19 years old (as of 2024)
    }

    @Test
    public void testAddFirestation() {
        // Arrange
        Firestation firestation = new Firestation("123 Main St", 1);

        when(data.getFirestations()).thenReturn(new ArrayList<>());

        // Act
        Firestation result = firestationService.addFirestation(firestation);

        // Assert
        assertNotNull(result);
        assertEquals("123 Main St", result.getAddress());
        assertEquals(1, result.getStation());
    }

    @Test
    public void testUpdateFirestation() {
        // Arrange
        Firestation existingFirestation = new Firestation("123 Main St", 1);
        Firestation updatedFirestation = new Firestation("123 Main St", 2);

        when(data.getFirestations()).thenReturn(Arrays.asList(existingFirestation));

        // Act
        Firestation result = firestationService.updateFirestation("123 Main St", 2);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getStation());
    }

    @Test
    public void testDeleteFirestation() {
        // Arrange
        Firestation firestation = new Firestation("123 Main St", 1);

        // Mocking data.getFirestations() to return a modifiable list
        List<Firestation> firestationsList = new ArrayList<>();
        firestationsList.add(firestation);
        when(data.getFirestations()).thenReturn(firestationsList);

        // Act
        boolean result = firestationService.deleteFirestation("123 Main St");

        // Assert
        assertTrue(result);
    }

}
