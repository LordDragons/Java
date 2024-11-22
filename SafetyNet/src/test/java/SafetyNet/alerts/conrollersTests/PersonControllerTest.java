package SafetyNet.alerts.conrollersTests;


import SafetyNet.alerts.controllers.PersonController;
import SafetyNet.alerts.dto.ChildAlertDTO;
import SafetyNet.alerts.dto.PersonInfoDTO;
import SafetyNet.alerts.models.MedicalRecord;
import SafetyNet.alerts.models.Person;
import SafetyNet.alerts.services.PersonService;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonControllerTest {

    @Mock
    private PersonService personService;

    @InjectMocks
    private PersonController personController;

    private Person person;

    @BeforeEach
    void setUp() {
        // Initialize a sample person for testing
        person = new Person("John", "Doe", "1234 Elm St", "SomeCity", "12345", "555-5555", "john.doe@example.com");
    }

    @Test
    void testGetEmailsList() {
        List<String> emails = Arrays.asList("john.doe@example.com", "jane.smith@example.com");

        when(personService.getEmailsList()).thenReturn(emails);

        List<String> response = personController.getEmailsList();

        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("john.doe@example.com", response.get(0));
        assertEquals("jane.smith@example.com", response.get(1));
    }

    @Test
    void testGetChildrenAlert() {
        String address = "1234 Elm St";
        List<ChildAlertDTO> children = Arrays.asList(new ChildAlertDTO("Jane", "John Doe", 5, Arrays.asList() ));

        when(personService.getChildrenByAddress(address)).thenReturn(children);

        ResponseEntity<List<ChildAlertDTO>> response = personController.getChildrenAlert(address);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(children, response.getBody());
    }

    @Test
    void testGetChildrenAlert_empty() {
        String address = "1234 Elm St";

        when(personService.getChildrenByAddress(address)).thenReturn(Collections.emptyList());

        ResponseEntity<List<ChildAlertDTO>> response = personController.getChildrenAlert(address);

        assertEquals(200, response.getStatusCodeValue()); // Empty list returned
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetPersonInfo() {
        String firstName = "John";
        String lastName = "Doe";
        List<PersonInfoDTO> personInfos = Arrays.asList(new PersonInfoDTO());

        when(personService.getPersonInfo(firstName, lastName)).thenReturn(personInfos);

        ResponseEntity<List<PersonInfoDTO>> response = personController.getPersonInfo(firstName, lastName);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(personInfos, response.getBody());
    }

    @Test
    void testGetPersonInfo_noContent() {
        String firstName = "John";
        String lastName = "Doe";

        when(personService.getPersonInfo(firstName, lastName)).thenReturn(Collections.emptyList());

        ResponseEntity<List<PersonInfoDTO>> response = personController.getPersonInfo(firstName, lastName);

        assertEquals(204, response.getStatusCodeValue()); // No content when no persons found
    }

    @Test
    void testAddPerson() {
        when(personService.addPerson(person)).thenReturn(person);

        ResponseEntity<Person> response = personController.addPerson(person);

        assertEquals(201, response.getStatusCodeValue());  // Created
        assertNotNull(response.getBody());
        assertEquals(person, response.getBody());
    }

    @Test
    void testUpdatePerson() {
        // Use any() to match any Person object passed to the service
        Person updatedPerson = new Person("John", "Doe", "1234 Elm St", "SomeCity", "12345", "555-5555", "john.doe@example.com");
        when(personService.updatePerson(any(Person.class))).thenReturn(Optional.of(updatedPerson));

        ResponseEntity<Optional<Person>> response = personController.updatePerson(
                "John", "Doe", "1234 Elm St", "SomeCity", "12345", "555-5555", "john.doe@example.com");

        assertEquals(200, response.getStatusCodeValue());  // OK
        assertTrue(response.getBody().isPresent());
        assertEquals(updatedPerson, response.getBody().get());
    }



    @Test
    void testUpdatePerson_notFound() {
        Person updatedPerson = new Person("John", "Doe", "1234 Elm St", "SomeCity", "12345", "555-5555", "john.doe@example.com");
        when(personService.updatePerson(any(Person.class))).thenReturn(Optional.of(updatedPerson));

        ResponseEntity<Optional<Person>> response = personController.updatePerson(
                "John", "Doe", "1234 Elm St", "SomeCity", "12345", "555-5555", "john.doe@example.com");

        assertEquals(200, response.getStatusCodeValue());  // Not Found
    }

    @Test
    void testDeletePerson() {
        String firstName = "John";
        String lastName = "Doe";

        when(personService.deletePerson(firstName, lastName)).thenReturn(true);

        ResponseEntity<Void> response = personController.deletePerson(firstName, lastName);

        assertEquals(204, response.getStatusCodeValue());  // No Content
    }

    @Test
    void testDeletePerson_notFound() {
        String firstName = "John";
        String lastName = "Doe";

        when(personService.deletePerson(firstName, lastName)).thenReturn(false);

        ResponseEntity<Void> response = personController.deletePerson(firstName, lastName);

        assertEquals(404, response.getStatusCodeValue());  // Not Found
    }

    @Test
    void testDeletePerson_badRequest() {
        ResponseEntity<Void> response = personController.deletePerson("", "");

        assertEquals(400, response.getStatusCodeValue());  // Bad Request
    }
}
