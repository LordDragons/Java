package SafetyNet.alerts.servicesTests;

import SafetyNet.alerts.dto.ChildAlertDTO;
import SafetyNet.alerts.models.MedicalRecord;
import SafetyNet.alerts.services.MedicalRecordService;
import SafetyNet.alerts.services.PersonService;
import SafetyNet.alerts.models.Person;
import SafetyNet.alerts.repositorys.MedicalRecordRepositoryImpl;
import SafetyNet.alerts.repositorys.PersonRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonServiceTest {

    @Mock
    private MedicalRecordService medicalRecordService;

    @Mock
    private PersonRepositoryImpl personRepositoryImpl;

    @Mock
    private MedicalRecordRepositoryImpl medicalRecordRepositoryImpl;

    @InjectMocks
    private PersonService personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getEmailsList_ShouldReturnEmailList() {
        List<String> emails = List.of("email1@test.com", "email2@test.com");
        when(personRepositoryImpl.getAllEmails()).thenReturn(emails);

        List<String> result = personService.getEmailsList();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("email1@test.com", result.get(0));
        verify(personRepositoryImpl, times(1)).getAllEmails();
    }

    @Test
    void getChildrenByAddress_ShouldReturnChildrenList() {
        String address = "123 Main St";
        Person person1 = new Person("John", "Doe", address, "City", "12345", "123-456-7890", "john.doe@test.com");
        Person person2 = new Person("Jane", "Doe", address, "City", "12345", "123-456-7891", "jane.doe@test.com");
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/2015", List.of("Med1"), List.of("Allergy1"));

        when(personRepositoryImpl.findByAddress(Collections.singletonList(address))).thenReturn(List.of(person1, person2));
        when(medicalRecordRepositoryImpl.findByFirstNameAndLastName("John", "Doe")).thenReturn(List.of(medicalRecord));

        List<ChildAlertDTO> result = personService.getChildrenByAddress(address);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        verify(personRepositoryImpl, times(1)).findByAddress(Collections.singletonList(address));
        verify(medicalRecordRepositoryImpl, times(1)).findByFirstNameAndLastName("John", "Doe");
    }

    @Test
    void addPerson_ShouldAddAndReturnPerson() {
        Person person = new Person("John", "Doe", "123 Main St", "City", "12345", "123-456-7890", "john.doe@test.com");

        List<Person> persons = new ArrayList<>();
        when(personRepositoryImpl.getPersons()).thenReturn(persons);

        Person result = personService.addPerson(person);

        assertNotNull(result);
        assertEquals(person, result);
        assertEquals(1, persons.size());
        verify(personRepositoryImpl, times(1)).getPersons();
    }

    @Test
    void updatePerson_ShouldUpdateAndReturnPerson() {
        Person existingPerson = new Person("John", "Doe", "123 Main St", "City", "12345", "123-456-7890", "john.doe@test.com");
        Person updatedPerson = new Person("John", "Doe", "456 Elm St", "NewCity", "67890", "098-765-4321", "john.new@test.com");

        List<Person> persons = new ArrayList<>(List.of(existingPerson));
        when(personRepositoryImpl.getPersons()).thenReturn(persons);

        Optional<Person> result = personService.updatePerson(updatedPerson);

        assertTrue(result.isPresent());
        assertEquals("456 Elm St", result.get().getAddress());
        verify(personRepositoryImpl, times(1)).getPersons();
    }

    @Test
    void deletePerson_ShouldRemovePersonAndReturnTrue() {
        Person person = new Person("John", "Doe", "123 Main St", "City", "12345", "123-456-7890", "john.doe@test.com");

        List<Person> persons = new ArrayList<>(List.of(person));
        when(personRepositoryImpl.getPersons()).thenReturn(persons);

        boolean result = personService.deletePerson("John", "Doe");

        assertTrue(result);
        assertTrue(persons.isEmpty());
        verify(personRepositoryImpl, times(1)).getPersons();
    }

    @Test
    void deletePerson_ShouldReturnFalseIfPersonNotFound() {
        List<Person> persons = new ArrayList<>();
        when(personRepositoryImpl.getPersons()).thenReturn(persons);

        boolean result = personService.deletePerson("John", "Doe");

        assertFalse(result);
        verify(personRepositoryImpl, times(1)).getPersons();
    }
}
