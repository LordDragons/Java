package SafetyNet.alerts.dtoTests;

import SafetyNet.alerts.dto.HouseDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HouseDTOTest {

    @Test
    public void testHouseDTOConstructor() {
        // Arrange
        List<HouseDTO.Resident> residents = Arrays.asList(
                new HouseDTO.Resident("John", "Doe", "123-456-7890", 30, Arrays.asList("Med1"), Arrays.asList("Peanuts")),
                new HouseDTO.Resident("Jane", "Doe", "987-654-3210", 25, Arrays.asList("Med2"), Arrays.asList("Shellfish"))
        );
        HouseDTO houseDTO = new HouseDTO("123 Main St", residents);

        // Act and Assert
        assertEquals("123 Main St", houseDTO.getAddress());
        assertEquals(2, houseDTO.getResidents().size());

        HouseDTO.Resident resident = houseDTO.getResidents().get(0);
        assertEquals("John", resident.getFirstName());
        assertEquals("Doe", resident.getLastName());
        assertEquals("123-456-7890", resident.getPhone());
        assertEquals(30, resident.getAge());
        assertTrue(resident.getMedications().contains("Med1"));
        assertTrue(resident.getAllergies().contains("Peanuts"));
    }

    @Test
    public void testSettersAndGetters() {
        // Arrange
        HouseDTO houseDTO = new HouseDTO();
        List<HouseDTO.Resident> residents = Arrays.asList(
                new HouseDTO.Resident("Alice", "Smith", "555-1234", 40, Arrays.asList("Med1"), Arrays.asList("Dust")),
                new HouseDTO.Resident("Bob", "Smith", "555-5678", 50, Arrays.asList("Med2"), Arrays.asList("Pollen"))
        );

        // Act
        houseDTO.setAddress("456 Oak St");
        houseDTO.setResidents(residents);

        // Assert
        assertEquals("456 Oak St", houseDTO.getAddress());
        assertEquals(2, houseDTO.getResidents().size());

        HouseDTO.Resident resident = houseDTO.getResidents().get(0);
        assertEquals("Alice", resident.getFirstName());
        assertEquals("Smith", resident.getLastName());
        assertEquals("555-1234", resident.getPhone());
        assertEquals(40, resident.getAge());
        assertTrue(resident.getMedications().contains("Med1"));
        assertTrue(resident.getAllergies().contains("Dust"));
    }
}

