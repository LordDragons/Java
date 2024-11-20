package SafetyNet.alerts.repositorys;



import SafetyNet.alerts.models.Data;
import SafetyNet.alerts.models.Firestation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FirestationRepositoryImpl implements FirestationRepository {

    private final Data data;

    public FirestationRepositoryImpl(Data data) {
        this.data = data;
    }

    @Override
    public List<String> findAddressesByStationNumber(int station) {
        return data.getFirestations().stream()
                .filter(firestation -> firestation.getStation().equals(station))
                .map(Firestation::getAddress)
                .collect(Collectors.toList());
    }
}

