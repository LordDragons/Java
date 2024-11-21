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

    // Ajouter un dossier médical
    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        data.getMedicalrecords().add(medicalRecord);
        return medicalRecord;
    }

    // Mettre à jour un dossier médical existant
    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {
        return data.getMedicalrecords().stream()
                .filter(record -> record.getFirstName().equalsIgnoreCase(medicalRecord.getFirstName()) &&
                        record.getLastName().equalsIgnoreCase(medicalRecord.getLastName()))
                .findFirst()
                .map(existingRecord -> {
                    existingRecord.setBirthDate(medicalRecord.getBirthDate());
                    existingRecord.setMedications(medicalRecord.getMedications());
                    existingRecord.setAllergies(medicalRecord.getAllergies());
                    return existingRecord;
                }).orElse(null);
    }

    // Supprimer un dossier médical
    public boolean deleteMedicalRecord(String firstName, String lastName) {
        return data.getMedicalrecords().removeIf(record -> record.getFirstName().equalsIgnoreCase(firstName) &&
                record.getLastName().equalsIgnoreCase(lastName));
    }
}

