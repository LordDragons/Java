package SafetyNet.alerts.services;

import SafetyNet.alerts.models.Data;
import SafetyNet.alerts.models.MedicalRecord;
import SafetyNet.alerts.models.Person;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MedicalRecordService {

    private final Data data;
    private Map<String, MedicalRecord> medicalRecordCache;

    public MedicalRecordService(Data data) {
        this.data = data;
    }

    @PostConstruct
    public void initializeCache() {
        medicalRecordCache = data.getMedicalrecords().stream()
                .collect(Collectors.toMap(
                        record -> record.getFirstName() + " " + record.getLastName(),
                        record -> record
                ));
    }

    // Méthode pour récupérer un dossier médical pour une personne donnée
    public MedicalRecord getMedicalRecordForPerson(Person person) {
        return medicalRecordCache.get(person.getFirstName() + " " + person.getLastName());
    }
}

