package SafetyNet.alerts.repositorys;



import SafetyNet.alerts.models.Firestation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FirestationRepository {

    List<String> findAddressesByStationNumber(int stationNumber);

    void save(Firestation firestation);


    List<Firestation> findByAddress(List<String> addresses);

    void delete(Firestation firestation);

    List<Firestation> getFirestations();
}
