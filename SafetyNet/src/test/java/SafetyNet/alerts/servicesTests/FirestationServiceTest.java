package SafetyNet.alerts.servicesTests;


import SafetyNet.alerts.dto.FirestationDTO;
import SafetyNet.alerts.dto.HouseDTO;
import SafetyNet.alerts.models.Firestation;
import SafetyNet.alerts.models.MedicalRecord;
import SafetyNet.alerts.models.Person;
import SafetyNet.alerts.repositorys.FirestationRepository;
import SafetyNet.alerts.repositorys.MedicalRecordRepositoryImpl;
import SafetyNet.alerts.repositorys.PersonRepositoryImpl;
import SafetyNet.alerts.services.FirestationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FirestationServiceTest {

    @Mock
    private FirestationRepository firestationRepository;

    @Mock
    private PersonRepositoryImpl personRepositoryImpl;

    @Mock
    private MedicalRecordRepositoryImpl medicalRecordRepositoryImpl;

    @InjectMocks
    private FirestationService firestationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFirestations_ShouldReturnListOfFirestations() {
        Firestation firestation1 = new Firestation("123 Main St", 1);
        Firestation firestation2 = new Firestation("456 Elm St", 2);

        when(firestationRepository.getFirestations()).thenReturn(Arrays.asList(firestation1, firestation2));

        List<Firestation> result = firestationService.getFirestations();

        assertEquals(2, result.size());
        verify(firestationRepository, times(1)).getFirestations();
    }

    @Test
    void getPhonesByFirestation_ShouldReturnListOfPhones() {
        int stationNumber = 1;
        List<String> addresses = Arrays.asList("123 Main St", "456 Elm St");
        List<String> phones = Arrays.asList("123-456-7890", "098-765-4321");

        when(firestationRepository.findAddressesByStationNumber(stationNumber)).thenReturn(addresses);
        when(personRepositoryImpl.findPhonesByAddresses(addresses)).thenReturn(phones);

        List<String> result = firestationService.getPhonesByFirestation(stationNumber);

        assertEquals(2, result.size());
        assertTrue(result.contains("123-456-7890"));
        verify(firestationRepository, times(1)).findAddressesByStationNumber(stationNumber);
        verify(personRepositoryImpl, times(1)).findPhonesByAddresses(addresses);
    }

    @Test
    void getHouseholdsByStations_ShouldReturnListOfHouseDTOs() {
        List<Integer> stationNumbers = Arrays.asList(1, 2);
        String address1 = "123 Main St";
        String address2 = "456 Elm St";

        when(firestationRepository.findAddressesByStationNumber(1)).thenReturn(Collections.singletonList(address1));
        when(firestationRepository.findAddressesByStationNumber(2)).thenReturn(Collections.singletonList(address2));

        when(personRepositoryImpl.findByAddress(Collections.singletonList(address1))).thenReturn(Collections.emptyList());
        when(personRepositoryImpl.findByAddress(Collections.singletonList(address2))).thenReturn(Collections.emptyList());

        List<HouseDTO> result = firestationService.getHouseholdsByStations(stationNumbers);

        assertEquals(2, result.size());
        assertEquals(address1, result.get(0).getAddress());
        assertEquals(address2, result.get(1).getAddress());
    }

    @Test
    void createResidentFromPerson_ShouldReturnResident() {
        Person person = new Person("John", "Doe", "123 Main St", "City", "12345", "123-456-7890", "john.doe@example.com");
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/2000", List.of("med1"), List.of("allergy1"));

        when(medicalRecordRepositoryImpl.findByFirstNameAndLastName(person.getFirstName(), person.getLastName()))
                .thenReturn(List.of(medicalRecord));

        HouseDTO.Resident resident = firestationService.createResidentFromPerson(person);

        assertNotNull(resident);
        assertEquals("John", resident.getFirstName());
        assertEquals("Doe", resident.getLastName());
        assertEquals("123-456-7890", resident.getPhone());
        assertEquals(Period.between(LocalDate.of(2000, 1, 1), LocalDate.now()).getYears(), resident.getAge());
        assertTrue(resident.getMedications().contains("med1"));
        assertTrue(resident.getAllergies().contains("allergy1"));
    }

    @Test
    void createResidentFromPerson_ShouldReturnNullWhenMedicalRecordMissing() {
        Person person = new Person("Jane", "Doe", "123 Main St", "City", "12345", "123-456-7890", "jane.doe@example.com");

        when(medicalRecordRepositoryImpl.findByFirstNameAndLastName(person.getFirstName(), person.getLastName()))
                .thenReturn(Collections.emptyList());

        HouseDTO.Resident resident = firestationService.createResidentFromPerson(person);

        assertNull(resident);
        verify(medicalRecordRepositoryImpl, times(1)).findByFirstNameAndLastName("Jane", "Doe");
    }

    @Test
    void getPersonsCoveredByStation_ShouldReturnFirestationDTO() {
        int stationNumber = 1;
        List<String> addresses = List.of("123 Main St");
        Person person = new Person("John", "Doe", "123 Main St", "City", "12345", "123-456-7890", "john.doe@example.com");
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/2000", List.of("med1"), List.of("allergy1"));

        when(firestationRepository.findAddressesByStationNumber(stationNumber)).thenReturn(addresses);
        when(personRepositoryImpl.findByAddress(addresses)).thenReturn(List.of(person));
        when(medicalRecordRepositoryImpl.findByFirstNameAndLastName("John", "Doe")).thenReturn(List.of(medicalRecord));

        FirestationDTO result = firestationService.getPersonsCoveredByStation(stationNumber);

        assertNotNull(result);
        assertEquals(1, result.getNumberOfAdults());
        assertEquals(0, result.getNumberOfChildren());
        assertEquals(1, result.getResidents().size());
    }

    @Test
    void getFireDetails_ShouldReturnFirestationDTO() {
        String address = "123 Main St";
        Firestation firestation = new Firestation(address, 1);
        Person person = new Person("John", "Doe", address, "City", "12345", "123-456-7890", "john.doe@example.com");
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/2000", List.of("med1"), List.of("allergy1"));

        when(firestationRepository.findByAddress(Collections.singletonList(address))).thenReturn(List.of(firestation));
        when(personRepositoryImpl.findByAddress(List.of(address))).thenReturn(List.of(person));
        when(medicalRecordRepositoryImpl.findByFirstNameAndLastName("John", "Doe")).thenReturn(List.of(medicalRecord));

        FirestationDTO result = firestationService.getFireDetails(address);

        assertNotNull(result);
        assertEquals(address, result.getAddress());
        assertEquals(1, result.getNumberOfAdults());
        assertEquals(0, result.getNumberOfChildren());
        assertEquals(1, result.getResidents().size());
    }

    @Test
    void getFireDetails_ShouldReturnNullWhenNoFirestationFound() {
        String address = "Nonexistent Address";

        when(firestationRepository.findByAddress(Collections.singletonList(address))).thenReturn(Collections.emptyList());

        FirestationDTO result = firestationService.getFireDetails(address);

        assertNull(result);
    }


    @Test
    void addFirestation_ShouldAddAndReturnFirestation() {
        Firestation firestation = new Firestation("123 Main St", 1);

        firestationService.addFirestation(firestation);

        verify(firestationRepository, times(1)).save(firestation);
    }

    @Test
    void updateFirestation_ShouldUpdateAndReturnFirestation() {
        String address = "123 Main St";
        int newStationNumber = 2;
        Firestation firestation = new Firestation(address, 1);

        // Simule le comportement du repository pour retourner une liste contenant un Firestation
        when(firestationRepository.findByAddress(Collections.singletonList(address)))
                .thenReturn(Collections.singletonList(firestation));

        Firestation result = firestationService.updateFirestation(address, newStationNumber);

        assertNotNull(result);
        assertEquals(newStationNumber, result.getStation());
        verify(firestationRepository, times(1)).save(firestation);
    }


    @Test
    void deleteFirestation_ShouldDeleteFirestation() {
        String address = "123 Main St";
        Firestation firestation = new Firestation(address, 1);

        // Simule le comportement du repository pour retourner une liste contenant un Firestation
        when(firestationRepository.findByAddress(Collections.singletonList(address)))
                .thenReturn(Collections.singletonList(firestation));

        boolean result = firestationService.deleteFirestation(address);

        assertTrue(result); // Vérifie que la suppression retourne true
        verify(firestationRepository, times(1)).delete(firestation); // Vérifie que la méthode delete a été appelée
    }


    @Test
    void deleteFirestation_ShouldReturnFalseIfNotFound() {
        String address = "Nonexistent Address";

        when(firestationRepository.findByAddress(Collections.singletonList(address))).thenReturn(Collections.emptyList());

        boolean result = firestationService.deleteFirestation(address);

        assertFalse(result);
        verify(firestationRepository, never()).delete(any(Firestation.class));
    }
}
