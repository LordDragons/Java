package SafetyNet.alerts.repositorysTests;

import SafetyNet.alerts.dto.PersonInfoDTO;
import SafetyNet.alerts.models.Data;
import SafetyNet.alerts.models.MedicalRecord;
import SafetyNet.alerts.models.Person;
import SafetyNet.alerts.repositorys.PersonRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonRepositoryImplTest {

    @Mock
    private Data data;

    private PersonRepositoryImpl personRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        personRepository = new PersonRepositoryImpl(data);
    }

    @Test
    void findPhonesByAddresses_ShouldReturnDistinctPhoneNumbers() {
        // Create and initialize Person objects
        Person person1 = new Person();
        person1.setAddress("123 Main St");
        person1.setPhone("123-456-7890");

        Person person2 = new Person();
        person2.setAddress("123 Main St");
        person2.setPhone("987-654-3210");

        Person person3 = new Person();
        person3.setAddress("456 Elm St");
        person3.setPhone("123-456-7890"); // Duplicate phone number for testing distinct filtering

        // Mock the data source to return the prepared objects
        List<Person> persons = Arrays.asList(person1, person2, person3);
        when(data.getPersons()).thenReturn(persons);

        // Call the method under test
        List<String> result = personRepository.findPhonesByAddresses(List.of("123 Main St"));

        // Verify the result
        assertEquals(2, result.size()); // Expecting 2 distinct phone numbers for the given address
        assertTrue(result.contains("123-456-7890"));
        assertTrue(result.contains("987-654-3210"));
    }



    @Test
    void findAll_ShouldReturnAllPersons() {
        List<Person> persons = Arrays.asList(
                new Person(),
                new Person()
        );

        when(data.getPersons()).thenReturn(persons);

        List<Person> result = personRepository.findAll();

        assertEquals(2, result.size());
        assertEquals(persons, result);
    }

    @Test
    void findByEmail_ShouldReturnPersonWhenEmailMatches() {
        Person person = new Person();
        person.setEmail("email1@test.com"); // Set the email field to avoid null

        when(data.getPersons()).thenReturn(Collections.singletonList(person));

        Optional<Person> result = personRepository.findByEmail("email1@test.com");

        assertTrue(result.isPresent());
        assertEquals(person, result.get());
    }


    @Test
    void findByEmail_ShouldReturnEmptyWhenNoMatch() {
        when(data.getPersons()).thenReturn(Collections.emptyList());

        Optional<Person> result = personRepository.findByEmail("email1@test.com");

        assertFalse(result.isPresent());
    }

    @Test
    void findByAddress_ShouldReturnPersonsMatchingAddresses() {
        // Set up persons with addresses
        Person person1 = new Person();
        person1.setAddress("123 Main St");
        person1.setFirstName("John");  // Set the first name to match your assertion

        Person person2 = new Person();
        person2.setAddress("456 Elm St");

        List<Person> persons = Arrays.asList(person1, person2);

        when(data.getPersons()).thenReturn(persons);

        // Find persons matching the address "123 Main St"
        List<Person> result = personRepository.findByAddress(List.of("123 Main St"));

        assertEquals(1, result.size()); // Expect one match
        assertEquals("John", result.get(0).getFirstName()); // Verify the first name of the match
    }


    @Test
    void getAllEmails_ShouldReturnAllEmails() {
        // Set up persons with email addresses
        Person person1 = new Person();
        person1.setEmail("email1@test.com");  // Set email for person1

        Person person2 = new Person();
        person2.setEmail("email2@test.com");  // Set email for person2

        List<Person> persons = Arrays.asList(person1, person2);

        when(data.getPersons()).thenReturn(persons);

        // Get all emails
        List<String> result = personRepository.getAllEmails();

        // Verify the size and the presence of emails in the result
        assertEquals(2, result.size());  // Expect two emails
        assertTrue(result.contains("email1@test.com"));  // Check if email1 is in the result
        assertTrue(result.contains("email2@test.com"));  // Check if email2 is in the result
    }


    @Test
    void getPersonInfo_ShouldReturnPersonInfoWhenDataExists() {
        // Create and initialize the Person object
        Person person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("123 Main St");
        person.setEmail("email1@test.com");

        // Create and initialize the MedicalRecord object
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "05/10/1985", List.of("Med1"), List.of("Allergy1"));

        // Mock the data source to return the prepared objects
        when(data.getPersons()).thenReturn(Collections.singletonList(person));
        when(data.getMedicalrecords()).thenReturn(Collections.singletonList(medicalRecord));

        // Call the method under test
        List<PersonInfoDTO> result = personRepository.getPersonInfo("John", "Doe");

        // Verify the result
        assertEquals(1, result.size());
        PersonInfoDTO dto = result.get(0);

        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("123 Main St", dto.getAddress());
        assertEquals("email1@test.com", dto.getEmail());
        assertEquals(39, dto.getAge()); // Update this to match the actual age based on the current year
        assertEquals(List.of("Med1"), dto.getMedications());
        assertEquals(List.of("Allergy1"), dto.getAllergies());
    }


    @Test
    void getPersonInfo_ShouldReturnEmptyWhenNoDataFound() {
        when(data.getPersons()).thenReturn(Collections.emptyList());
        when(data.getMedicalrecords()).thenReturn(Collections.emptyList());

        List<PersonInfoDTO> result = personRepository.getPersonInfo("John", "Doe");

        assertTrue(result.isEmpty());
    }
}
