package SafetyNet.alerts.repositorys;

import SafetyNet.alerts.models.Data;
import SafetyNet.alerts.models.MedicalRecord;

import java.util.List;

public class MedicalRecordRepository {
    private final List<MedicalRecord> medicalRecords;

    public MedicalRecordRepository(Data data) {
        this.medicalRecords = data.getMedicalrecords();  // Initialisation à partir de vos données JSON
    }

    public List<MedicalRecord> findAll() {
        return medicalRecords;
    }

    public MedicalRecord findByFirstNameAndLastName(String firstName, String lastName) {
        return medicalRecords.stream()
                .filter(m -> m.getFirstName().equalsIgnoreCase(firstName))
                .filter(m -> m.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);
    }
}

