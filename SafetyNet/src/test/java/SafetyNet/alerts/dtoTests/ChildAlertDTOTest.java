package SafetyNet.alerts.dtoTests;

import SafetyNet.alerts.dto.ChildAlertDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ChildAlertDTOTest {

    @Test
    public void testChildAlertDTO() {
        // Arrange
        List<String> householdMembers = Arrays.asList("John Doe", "Jane Doe");
        ChildAlertDTO childAlertDTO = new ChildAlertDTO("Tom", "Smith", 10, householdMembers);

        // Act and Assert
        assertEquals("Tom", childAlertDTO.getFirstName());
        assertEquals("Smith", childAlertDTO.getLastName());
        assertEquals(10, childAlertDTO.getAge());
        assertEquals(householdMembers, childAlertDTO.getHouseholdMembers());
    }

    @Test
    public void testSettersAndGetters() {
        // Arrange
        ChildAlertDTO childAlertDTO = new ChildAlertDTO("Tom", "Smith", 10, null);

        // Act
        childAlertDTO.setFirstName("Jerry");
        childAlertDTO.setLastName("Miller");
        childAlertDTO.setAge(11);
        List<String> newHousehold = Arrays.asList("Alice", "Bob");
        childAlertDTO.setHouseholdMembers(newHousehold);

        // Assert
        assertEquals("Jerry", childAlertDTO.getFirstName());
        assertEquals("Miller", childAlertDTO.getLastName());
        assertEquals(11, childAlertDTO.getAge());
        assertEquals(newHousehold, childAlertDTO.getHouseholdMembers());
    }
}
