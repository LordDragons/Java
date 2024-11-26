package SafetyNet.alerts.repositorysTests;

import SafetyNet.alerts.models.Data;
import SafetyNet.alerts.models.Firestation;
import SafetyNet.alerts.repositorys.FirestationRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FirestationRepositoryImplTest {

    @Mock
    private Data data;

    private FirestationRepositoryImpl firestationRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        firestationRepository = new FirestationRepositoryImpl(data);
    }

    @Test
    void findAddressesByStationNumber_ShouldReturnAddressesForGivenStation() {
        Firestation firestation1 = new Firestation("123 Main St", 1);
        Firestation firestation2 = new Firestation("456 Elm St", 1);
        Firestation firestation3 = new Firestation("789 Oak St", 2);

        when(data.getFirestations()).thenReturn(Arrays.asList(firestation1, firestation2, firestation3));

        List<String> result = firestationRepository.findAddressesByStationNumber(1);

        assertEquals(2, result.size());
        assertTrue(result.contains("123 Main St"));
        assertTrue(result.contains("456 Elm St"));
        verify(data, times(1)).getFirestations();
    }

    @Test
    void save_ShouldAddOrUpdateFirestation() {
        Firestation firestation1 = new Firestation("123 Main St", 1);

        firestationRepository.save(firestation1);

        List<Firestation> result = firestationRepository.getFirestations();
        assertEquals(1, result.size());
        assertEquals(firestation1, result.get(0));

        // Update the same firestation
        Firestation updatedFirestation = new Firestation("123 Main St", 2);
        firestationRepository.save(updatedFirestation);

        result = firestationRepository.getFirestations();
        assertEquals(1, result.size());
        assertEquals(updatedFirestation, result.get(0));
    }

    @Test
    void findByAddress_ShouldReturnFirestationsMatchingAddresses() {
        Firestation firestation1 = new Firestation("123 Main St", 1);
        Firestation firestation2 = new Firestation("456 Elm St", 2);

        when(data.getFirestations()).thenReturn(Arrays.asList(firestation1, firestation2));

        List<Firestation> result = firestationRepository.findByAddress(Collections.singletonList("123 Main St"));

        assertEquals(1, result.size());
        assertEquals(firestation1, result.get(0));
        verify(data, times(1)).getFirestations();
    }

    @Test
    void delete_ShouldRemoveFirestation() {
        Firestation firestation1 = new Firestation("123 Main St", 1);

        firestationRepository.save(firestation1);
        assertEquals(1, firestationRepository.getFirestations().size());

        firestationRepository.delete(firestation1);
        assertEquals(0, firestationRepository.getFirestations().size());
    }

    @Test
    void getFirestations_ShouldReturnAllSavedFirestations() {
        Firestation firestation1 = new Firestation("123 Main St", 1);
        Firestation firestation2 = new Firestation("456 Elm St", 2);

        firestationRepository.save(firestation1);
        firestationRepository.save(firestation2);

        List<Firestation> result = firestationRepository.getFirestations();

        assertEquals(2, result.size());
        assertTrue(result.contains(firestation1));
        assertTrue(result.contains(firestation2));
    }
}
