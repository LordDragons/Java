package SafetyNet.alerts.repositorys;

import SafetyNet.alerts.dto.PersonInfoDTO;
import SafetyNet.alerts.models.Data;
import SafetyNet.alerts.models.MedicalRecord;
import SafetyNet.alerts.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static SafetyNet.alerts.services.PersonService.calculateAge;

@Repository
public class PersonRepositoryImpl implements PersonRepository {

    private final Data data;

    @Autowired
    public PersonRepositoryImpl(Data data) {
        this.data = data;
    }

    @Override
    public List<String> findPhonesByAddresses(List<String> addresses) {
        return data.getPersons().stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .map(Person::getPhone)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Person> findAll() {
        return data.getPersons();
    }

    @Override
    public Optional<Person> findByEmail(String email) {
        return data.getPersons().stream()
                .filter(person -> person.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<Person> findByAddress(List<String> addresses) {
        return data.getPersons().stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllEmails() {
        return data.getPersons().stream()
                .map(Person::getEmail)
                .collect(Collectors.toList());
    }

    @Override
    public Person findByFirstNameAndLastName(String firstName, String lastName) {
        return data.getPersons().stream()
                .filter(person -> person.getFirstName().equalsIgnoreCase(firstName))
                .filter(person -> person.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<PersonInfoDTO> qetPersons() {
        return List.of();
    }

    public Collection<Person> getPersons() {
        return data.getPersons();
    }

    @Override
    public List<PersonInfoDTO> getPersonInfo(String firstName, String lastName) {
        // Recherche de la personne
        Person person = data.getPersons().stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName))
                .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);

        if (person == null) {
            return List.of(); // Aucun résultat si la personne n'est pas trouvée
        }

        // Recherche du dossier médical associé
        MedicalRecord medicalRecord = data.getMedicalrecords().stream()
                .filter(m -> m.getFirstName().equalsIgnoreCase(firstName))
                .filter(m -> m.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);

        // Construction du DTO
        PersonInfoDTO personInfoDTO = new PersonInfoDTO();
        personInfoDTO.setFirstName(person.getFirstName());
        personInfoDTO.setLastName(person.getLastName());
        personInfoDTO.setAddress(person.getAddress());
        personInfoDTO.setEmail(person.getEmail());

        if (medicalRecord != null) {
            personInfoDTO.setAge(calculateAge(medicalRecord.getBirthDate()));
            personInfoDTO.setMedications(medicalRecord.getMedications());
            personInfoDTO.setAllergies(medicalRecord.getAllergies());
        } else {
            personInfoDTO.setAge(-1); // Indique que l'âge est inconnu
            personInfoDTO.setMedications(List.of());
            personInfoDTO.setAllergies(List.of());
        }

        return List.of(personInfoDTO);
    }


}
