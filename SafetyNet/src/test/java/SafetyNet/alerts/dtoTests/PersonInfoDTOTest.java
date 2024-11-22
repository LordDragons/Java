package SafetyNet.alerts.dtoTests;

import SafetyNet.alerts.dto.PersonInfoDTO;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

public class PersonInfoDTOTest {

    @Test
    public void testPersonInfoDTOConstructorAndGetters() {
        // Arrange
        PersonInfoDTO person = new PersonInfoDTO();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("123 Main St");
        person.setAge(30);
        person.setEmail("john.doe@example.com");
        person.setMedications(Arrays.asList("Med1", "Med2"));
        person.setAllergies(Arrays.asList("Peanuts", "Shellfish"));

        // Act & Assert
        assertEquals("John", person.getFirstName());
        assertEquals("Doe", person.getLastName());
        assertEquals("123 Main St", person.getAddress());
        assertEquals(30, person.getAge());
        assertEquals("john.doe@example.com", person.getEmail());
        assertTrue(person.getMedications().contains("Med1"));
        assertTrue(person.getAllergies().contains("Peanuts"));
    }

    @Test
    public void testSettersAndToString() {
        // Arrange
        PersonInfoDTO person = new PersonInfoDTO();
        person.setFirstName("Jane");
        person.setLastName("Doe");
        person.setAddress("456 Oak St");
        person.setAge(25);
        person.setEmail("jane.doe@example.com");
        person.setMedications(Arrays.asList("Med3"));
        person.setAllergies(Arrays.asList("Dust"));

        // Act & Assert
        assertEquals("Jane", person.getFirstName());
        assertEquals("Doe", person.getLastName());
        assertEquals("456 Oak St", person.getAddress());
        assertEquals(25, person.getAge());
        assertEquals("jane.doe@example.com", person.getEmail());
        assertTrue(person.getMedications().contains("Med3"));
        assertTrue(person.getAllergies().contains("Dust"));

        // Assert toString() method
        String expectedToString = "PersonInfoDTO{firstName='Jane', lastName='Doe', address='456 Oak St', age=25, email='jane.doe@example.com', medications=[Med3], allergies=[Dust]}";
        assertEquals(expectedToString, person.toString());
    }
}

