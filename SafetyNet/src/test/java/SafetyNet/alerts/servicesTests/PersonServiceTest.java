package SafetyNet.alerts.servicesTests;

import SafetyNet.alerts.dto.PersonInfoDTO;
import SafetyNet.alerts.dto.ChildAlertDTO;
import SafetyNet.alerts.models.Person;
import SafetyNet.alerts.models.MedicalRecord;
import SafetyNet.alerts.repositorys.MedicalRecordRepositoryImpl;
import SafetyNet.alerts.services.PersonService;
import SafetyNet.alerts.services.MedicalRecordService;
import SafetyNet.alerts.repositorys.PersonRepositoryImpl;
import SafetyNet.alerts.models.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PersonServiceTest {

    @Mock
    private Data data;  // Mock Data to simulate data fetching

    @Mock
    private MedicalRecordService medicalRecordService;  // Mock the medical record service

    @Mock
    private PersonRepositoryImpl personRepositoryImpl;  // Mock the repository

    @Mock
    private MedicalRecordRepositoryImpl medicalRecordRepositoryImpl;

    private PersonService personService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        personService = new PersonService(data, medicalRecordService, personRepositoryImpl, medicalRecordRepositoryImpl);  // Initialize the service with mocked dependencies
    }


    @Test
    public void testGetEmailsList() {
        // Arrange
        Person person1 = new Person();
        person1.setEmail("person1@example.com");
        Person person2 = new Person();
        person2.setEmail("person2@example.com");
        when(data.getPersons()).thenReturn(List.of(person1, person2));

        // Act
        List<String> emails = personService.getEmailsList();

        // Assert
        assertNotNull(emails);
        assertEquals(2, emails.size());
        assertTrue(emails.contains("person1@example.com"));
        assertTrue(emails.contains("person2@example.com"));
    }

    @Test
    public void testGetChildrenByAddress() {
        // Arrange
        String address = "123 Main St";
        Person child = new Person();
        child.setFirstName("John");
        child.setLastName("Doe");
        child.setAddress(address);
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "04/15/2010", List.of(), List.of()); // Age <= 18
        when(personRepositoryImpl.findByAddress(List.of(address))).thenReturn(List.of(child));
        when(data.getMedicalrecords()).thenReturn(List.of(medicalRecord));

        // Act
        List<ChildAlertDTO> children = personService.getChildrenByAddress(address);

        // Assert
        assertNotNull(children);
        assertEquals(1, children.size());
        assertEquals("John", children.get(0).getFirstName());
        assertEquals("Doe", children.get(0).getLastName());
    }

    @Test
    public void testGetPersonInfo() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setEmail("john.doe@example.com");
        person.setAddress("123 Main St");
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "04/15/1990", List.of("Medication1"), List.of("Allergy1"));
        when(data.getPersons()).thenReturn(List.of(person));
        when(data.getMedicalrecords()).thenReturn(List.of(medicalRecord));

        // Act
        List<PersonInfoDTO> personInfo = personService.getPersonInfo(firstName, lastName);

        // Assert
        assertNotNull(personInfo);
        assertEquals(1, personInfo.size());
        assertEquals(firstName, personInfo.get(0).getFirstName());
        assertEquals(lastName, personInfo.get(0).getLastName());
        assertEquals("john.doe@example.com", personInfo.get(0).getEmail());
        assertEquals("123 Main St", personInfo.get(0).getAddress());
        assertEquals(34, personInfo.get(0).getAge()); // Assuming today's date is after 04/15/2024
        assertTrue(personInfo.get(0).getMedications().contains("Medication1"));
        assertTrue(personInfo.get(0).getAllergies().contains("Allergy1"));
    }

    @Test
    public void testAddPerson() {
        // Arrange
        Person newPerson = new Person();
        newPerson.setFirstName("Jane");
        newPerson.setLastName("Doe");

        // Use a mutable ArrayList
        List<Person> personList = new ArrayList<>();
        // You can add any initial people to the list if necessary
        personList.add(new Person());  // Adding a sample person, if needed

        when(data.getPersons()).thenReturn(personList);

        // Act
        Person addedPerson = personService.addPerson(newPerson);

        // Assert
        assertNotNull(addedPerson);
        assertEquals("Jane", addedPerson.getFirstName());
        assertEquals("Doe", addedPerson.getLastName());
        assertTrue(personList.contains(newPerson));  // Ensure the person was added to the list
    }


    @Test
    public void testUpdatePerson() {
        // Arrange
        Person existingPerson = new Person();
        existingPerson.setFirstName("John");
        existingPerson.setLastName("Doe");
        existingPerson.setEmail("john.doe@example.com");
        when(data.getPersons()).thenReturn(List.of(existingPerson));

        Person updatedPerson = new Person();
        updatedPerson.setFirstName("John");
        updatedPerson.setLastName("Doe");
        updatedPerson.setEmail("john.new@example.com");

        // Act
        Optional<Person> updated = personService.updatePerson(updatedPerson);

        // Assert
        assertTrue(updated.isPresent());
        assertEquals("john.new@example.com", updated.get().getEmail());
    }

    @Test
    public void testDeletePerson() {
        // Arrange
        Person personToDelete = new Person();
        personToDelete.setFirstName("John");
        personToDelete.setLastName("Doe");

        // Using a mutable ArrayList
        List<Person> personList = new ArrayList<>();
        personList.add(personToDelete);

        when(data.getPersons()).thenReturn(personList);

        // Act
        boolean isDeleted = personService.deletePerson("John", "Doe");

        // Assert
        assertTrue(isDeleted);
    }

}

