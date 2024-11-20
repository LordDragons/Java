package SafetyNet.alerts.repositorys;


import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FirestationRepository {
    List<String> findAddressesByStationNumber(int stationNumber);

}
