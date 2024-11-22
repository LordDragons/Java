package SafetyNet.alerts.dtoTests;

import SafetyNet.alerts.dto.FirestationDTO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FirestationDTOTest {

    @Test
    public void testFirestationDTOConstructor() {
        // Arrange
        List<FirestationDTO.ResidentInfo> residents = Arrays.asList(
                new FirestationDTO.ResidentInfo("John Doe", "123-456-7890", 30, Arrays.asList("Med1", "Med2"), Arrays.asList("Peanuts")),
                new FirestationDTO.ResidentInfo("Jane Doe", "987-654-3210", 25, Arrays.asList("Med3"), Arrays.asList("Shellfish"))
        );
        FirestationDTO firestationDTO = new FirestationDTO("123 Main St", 1, 2L, 2L, residents);

        // Act and Assert
        assertEquals("123 Main St", firestationDTO.getAddress());
        assertEquals(1, firestationDTO.getStation());
        assertEquals(2L, firestationDTO.getNumberOfAdults());
        assertEquals(2L, firestationDTO.getNumberOfChildren());
        assertEquals(2, firestationDTO.getResidents().size());

        FirestationDTO.ResidentInfo resident = firestationDTO.getResidents().get(0);
        assertEquals("John Doe", resident.getName());
        assertEquals("123-456-7890", resident.getPhone());
        assertEquals(30, resident.getAge());
        assertTrue(resident.getMedications().contains("Med1"));
        assertTrue(resident.getAllergies().contains("Peanuts"));
    }

    @Test
    public void testSettersAndGetters() {
        // Arrange
        FirestationDTO firestationDTO = new FirestationDTO();
        List<FirestationDTO.ResidentInfo> residents = Arrays.asList(
                new FirestationDTO.ResidentInfo("Alice", "555-1234", 40, Arrays.asList("Med1"), Arrays.asList("Dust")),
                new FirestationDTO.ResidentInfo("Bob", "555-5678", 50, Arrays.asList("Med2"), Arrays.asList("Pollen"))
        );

        // Act
        firestationDTO.setAddress("456 Oak St");
        firestationDTO.setStation(2);
        firestationDTO.setNumberOfAdults(1L);
        firestationDTO.setNumberOfChildren(1L);
        firestationDTO.setResidents(residents);

        // Assert
        assertEquals("456 Oak St", firestationDTO.getAddress());
        assertEquals(2, firestationDTO.getStation());
        assertEquals(1L, firestationDTO.getNumberOfAdults());
        assertEquals(1L, firestationDTO.getNumberOfChildren());
        assertEquals(2, firestationDTO.getResidents().size());

        FirestationDTO.ResidentInfo resident = firestationDTO.getResidents().get(0);
        assertEquals("Alice", resident.getName());
        assertEquals("555-1234", resident.getPhone());
        assertEquals(40, resident.getAge());
        assertTrue(resident.getMedications().contains("Med1"));
        assertTrue(resident.getAllergies().contains("Dust"));
    }
}

