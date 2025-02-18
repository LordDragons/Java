package SafetyNet.alerts.repositorys;

import SafetyNet.alerts.dto.HouseDTO;
import SafetyNet.alerts.models.MedicalRecord;
import SafetyNet.alerts.models.Person;

import java.util.Collection;
import java.util.List;


public interface MedicalRecordRepository {

    // Récupère tous les dossiers médicaux
    List<MedicalRecord> findAll();

    // Recherche un dossier médical par prénom et nom de famille
    List<MedicalRecord> findByFirstNameAndLastName(String firstName, String lastName);

    HouseDTO.Resident createResidentFromPerson(Person person);

    Collection<Object> getMedicalrecords();
}
