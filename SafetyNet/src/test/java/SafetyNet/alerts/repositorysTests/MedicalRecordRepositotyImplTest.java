package SafetyNet.alerts.repositorysTests;

import SafetyNet.alerts.dto.HouseDTO;
import SafetyNet.alerts.models.Data;
import SafetyNet.alerts.models.MedicalRecord;
import SafetyNet.alerts.models.Person;
import SafetyNet.alerts.repositorys.MedicalRecordRepositoryImpl;
import SafetyNet.alerts.services.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicalRecordRepositoryImplTest {

    @Mock
    private Data data;

    private MedicalRecordRepositoryImpl medicalRecordRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        medicalRecordRepository = new MedicalRecordRepositoryImpl(data);
    }

    @Test
    void findAll_ShouldReturnAllMedicalRecords() {
        MedicalRecord medicalRecord1 = new MedicalRecord("John", "Doe", "05/10/1985", List.of("Med1"), List.of("Allergy1"));
        MedicalRecord medicalRecord2 = new MedicalRecord("Jane", "Smith", "08/15/1990", List.of("Med2"), List.of("Allergy2"));

        when(data.getMedicalrecords()).thenReturn(Arrays.asList(medicalRecord1, medicalRecord2));

        List<MedicalRecord> result = medicalRecordRepository.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(medicalRecord1));
        assertTrue(result.contains(medicalRecord2));
        verify(data, times(1)).getMedicalrecords();
    }

    @Test
    void findByFirstNameAndLastName_ShouldReturnMatchingMedicalRecord() {
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "05/10/1995", List.of("Med1"), List.of("Allergy1"));

        when(data.getMedicalrecords()).thenReturn(Collections.singletonList(medicalRecord));

        List<MedicalRecord> result = medicalRecordRepository.findByFirstNameAndLastName("John", "Doe");

        assertEquals(1, result.size());
        assertEquals(medicalRecord, result.get(0));
        verify(data, times(1)).getMedicalrecords();
    }

    @Test
    void findByFirstNameAndLastName_ShouldReturnEmptyListWhenNoMatch() {
        MedicalRecord medicalRecord = new MedicalRecord("Jane", "Smith", "08/15/1990", List.of("Med2"), List.of("Allergy2"));

        when(data.getMedicalrecords()).thenReturn(Collections.singletonList(medicalRecord));

        List<MedicalRecord> result = medicalRecordRepository.findByFirstNameAndLastName("John", "Doe");

        assertTrue(result.isEmpty());
        verify(data, times(1)).getMedicalrecords();
    }

    @Test
    void createResidentFromPerson_ShouldReturnResidentWhenMedicalRecordExists() {
        // Création d'un objet Person avec des données correspondant au MedicalRecord
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setPhone("123-456-7890");


        // Création d'un MedicalRecord correspondant
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "05/10/1985", List.of("Med1"), List.of("Allergy1"));

        // Simulation des données renvoyées par data.getMedicalrecords()
        when(data.getMedicalrecords()).thenReturn(Collections.singletonList(medicalRecord));

        // Calcul de l'âge attendu
        int expectedAge = PersonService.calculateAge(medicalRecord.getBirthDate());

        // Appel de la méthode à tester
        HouseDTO.Resident result = medicalRecordRepository.createResidentFromPerson(person);

        // Assertions pour vérifier les résultats
        assertNotNull(result); // Vérifie que l'objet result n'est pas null
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("123-456-7890", result.getPhone());
        assertEquals(expectedAge, result.getAge());
        assertEquals(List.of("Med1"), result.getMedications());
        assertEquals(List.of("Allergy1"), result.getAllergies());
    }


    @Test
    void createResidentFromPerson_ShouldReturnNullWhenNoMedicalRecordFound() {
        Person person = new Person();

        when(data.getMedicalrecords()).thenReturn(Collections.emptyList());

        HouseDTO.Resident result = medicalRecordRepository.createResidentFromPerson(person);

        assertNull(result);
        verify(data, times(1)).getMedicalrecords();
    }
}
