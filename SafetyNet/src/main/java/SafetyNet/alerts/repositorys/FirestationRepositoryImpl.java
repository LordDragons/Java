package SafetyNet.alerts.repositorys;



import SafetyNet.alerts.models.Data;
import SafetyNet.alerts.models.Firestation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FirestationRepositoryImpl implements FirestationRepository {

    private final Data data;

    @Autowired
    public FirestationRepositoryImpl(Data data) {
        this.data = data;
    }
    private List<Firestation> firestations = new ArrayList<>();

    @Override
    public List<String> findAddressesByStationNumber(int station) {
        return data.getFirestations().stream()
                .filter(firestation -> firestation.getStation().equals(station))
                .map(Firestation::getAddress)
                .collect(Collectors.toList());
    }

    @Override
    public void save(Firestation firestation) {
        // Vérifie si la firestation existe déjà (par exemple, par adresse)
        for (int i = 0; i < firestations.size(); i++) {
            if (firestations.get(i).getAddress().equals(firestation.getAddress())) {
                // Si trouvée, on met à jour l'existant
                firestations.set(i, firestation);
                return;
            }
        }
        // Si non trouvée, on ajoute la nouvelle firestation
        firestations.add(firestation);
    }


    @Override
    public List<Firestation> findByAddress(List<String> addresses) {
        return data.getFirestations().stream()
                .filter(firestation -> addresses.contains(firestation.getAddress()))
                .collect(Collectors.toList());
    }


    @Override
    public void delete(Firestation firestation) {
        firestations.remove(firestation);
    }

    @Override
    public List<Firestation> getFirestations() {
        return firestations;
    }
}

