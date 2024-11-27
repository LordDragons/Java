package SafetyNet.alerts.services;

import SafetyNet.alerts.models.MedicalRecord;
import SafetyNet.alerts.models.Person;
import SafetyNet.alerts.repositorys.MedicalRecordRepositoryImpl;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepositoryImpl medicalRecordRepository;
    private Map<String, MedicalRecord> medicalRecordCache;

    public MedicalRecordService(MedicalRecordRepositoryImpl medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    @PostConstruct
    public void initializeCache() {
        // Initialiser le cache à partir des dossiers médicaux du dépôt
        medicalRecordCache = medicalRecordRepository.findAll().stream()
                .collect(Collectors.toMap(
                        record -> record.getFirstName() + " " + record.getLastName(),
                        record -> record
                ));
    }

    // Récupérer un dossier médical pour une personne donnée
    public MedicalRecord getMedicalRecordForPerson(Person person) {
        return medicalRecordCache.get(person.getFirstName() + " " + person.getLastName());
    }

    // Ajouter un dossier médical
    public MedicalRecord addMedicalRecord(MedicalRecord medicalRecord) {
        List<MedicalRecord> records = medicalRecordRepository.findAll();
        records.add(medicalRecord);
        medicalRecordCache.put(medicalRecord.getFirstName() + " " + medicalRecord.getLastName(), medicalRecord);
        return medicalRecord;
    }

    // Mettre à jour un dossier médical existant
    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {
        MedicalRecord existingRecord = medicalRecordRepository.findByFirstNameAndLastName(
                medicalRecord.getFirstName(),
                medicalRecord.getLastName()
        ).stream().findFirst().orElse(null);

        if (existingRecord != null) {
            existingRecord.setBirthDate(medicalRecord.getBirthDate());
            existingRecord.setMedications(medicalRecord.getMedications());
            existingRecord.setAllergies(medicalRecord.getAllergies());
            medicalRecordCache.put(existingRecord.getFirstName() + " " + existingRecord.getLastName(), existingRecord);
        }
        return existingRecord;
    }

    // Supprimer un dossier médical
    public boolean deleteMedicalRecord(String firstName, String lastName) {
        List<MedicalRecord> records = medicalRecordRepository.findAll();
        boolean isDeleted = records.removeIf(record ->
                record.getFirstName().equalsIgnoreCase(firstName) &&
                        record.getLastName().equalsIgnoreCase(lastName));
        if (isDeleted) {
            medicalRecordCache.remove(firstName + " " + lastName);
        }
        return isDeleted;
    }
}
