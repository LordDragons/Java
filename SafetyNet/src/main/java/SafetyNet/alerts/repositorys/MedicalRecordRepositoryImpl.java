package SafetyNet.alerts.repositorys;

import SafetyNet.alerts.dto.HouseDTO;
import SafetyNet.alerts.models.Data;
import SafetyNet.alerts.models.MedicalRecord;
import SafetyNet.alerts.models.Person;
import SafetyNet.alerts.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class MedicalRecordRepositoryImpl implements MedicalRecordRepository {

    private final Data data;

    private void logMissingMedicalRecord(Person person) {
    }

    @Autowired
    public MedicalRecordRepositoryImpl(Data data) {
        this.data = data;
    }

    @Override
    public List<MedicalRecord> findAll() {
        // Retourne tous les dossiers médicaux présents dans la base de données JSON
        return data.getMedicalrecords();
    }

    @Override
    public List<MedicalRecord> findByFirstNameAndLastName(String firstName, String lastName) {
        // Filtre les dossiers médicaux par prénom et nom de famille
        return data.getMedicalrecords().stream()
                .filter(m -> m.getFirstName().equalsIgnoreCase(firstName))
                .filter(m -> m.getLastName().equalsIgnoreCase(lastName))
                .collect(Collectors.toList());
    }


    public HouseDTO.Resident createResidentFromPerson(Person person) {
        MedicalRecord medicalRecord = data.getMedicalrecords().stream()
                .filter(m -> m.getFirstName().equalsIgnoreCase(person.getFirstName()))
                .filter(m -> m.getLastName().equalsIgnoreCase(person.getLastName()))
                .findFirst()
                .orElse(null);

        if (medicalRecord == null) {
            logMissingMedicalRecord(person);
            return null;
        }

        // Retourne un nouvel objet Resident avec les données récupérées
        return new HouseDTO.Resident(
                person.getFirstName(),
                person.getLastName(),
                person.getPhone(),
                PersonService.calculateAge(medicalRecord.getBirthDate()),
                medicalRecord.getMedications(),
                medicalRecord.getAllergies()
        );
    }

    @Override
    public Collection<Object> getMedicalrecords() {
        return List.of();
    }


}
