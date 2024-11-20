package SafetyNet.alerts.services;

import SafetyNet.alerts.dto.AgeCalculatorDTO;
import SafetyNet.alerts.dto.ChildAlertDTO;
import SafetyNet.alerts.dto.PersonInfoDTO;
import SafetyNet.alerts.models.Data;
import SafetyNet.alerts.models.MedicalRecord;
import SafetyNet.alerts.models.Person;
import SafetyNet.alerts.repositorys.PersonRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PersonService {

    private final Data data;
    private final MedicalRecordService medicalRecordService;  // Injection de dépendance
    private final PersonRepositoryImpl personRepositoryImpl;


    // Constructeur avec injection de MedicalRecordService
    @Autowired
    public PersonService(Data data, MedicalRecordService medicalRecordService, PersonRepositoryImpl personRepositoryImpl) {
        this.data = data;
        this.medicalRecordService = medicalRecordService;  // Assignation de la dépendance
        this.personRepositoryImpl = personRepositoryImpl;
    }

    // Méthode pour récupérer la liste des emails
    public List<String> getEmailsList() {
        return data.getPersons().stream()
                .map(Person::getEmail)
                .collect(Collectors.toList());
    }

    // Méthode pour récupérer les enfants d'une adresse
    public List<ChildAlertDTO> getChildrenByAddress(String address) {
        // Récupère toutes les personnes à l'adresse donnée
        List<Person> personsAtAddress = personRepositoryImpl.findByAddress(Collections.singletonList(address));
        List<ChildAlertDTO> children = new ArrayList<>();

        // Parcourt chaque personne pour vérifier les informations de dossier médical
        for (Person person : personsAtAddress) {
            try {
                // Récupération du dossier médical via le service MedicalRecordService pour chaque personne
                MedicalRecord medicalRecord = data.getMedicalrecords().stream()
                        .filter(m -> m.getFirstName().equalsIgnoreCase(person.getFirstName()))
                        .filter(m -> m.getLastName().equalsIgnoreCase(person.getLastName()))
                        .findFirst()
                        .orElse(null);

                // Si le dossier médical est manquant, on ignore cette personne
                if (medicalRecord == null) {
                    System.err.println("Dossier médical manquant pour " + person.getFirstName() + " " + person.getLastName());
                    continue;  // Passe à la personne suivante
                }

                // Vérifie si la personne est un enfant (âge <= 18)
                String birthDateString = medicalRecord.getBirthDate();
                int age = AgeCalculatorDTO.calculateAge(birthDateString);
                if (age <= 18) {
                    // Si c'est un enfant, on crée un ChildAlertDTO
                    List<String> householdMembers = new ArrayList<>();
                    for (Person householdMember : personsAtAddress) {
                        if (!householdMember.equals(person)) {
                            householdMembers.add(householdMember.getFirstName() + " " + householdMember.getLastName());
                        }
                    }
                    ChildAlertDTO child = new ChildAlertDTO(person.getFirstName(), person.getLastName(), age, householdMembers);
                    children.add(child);
                }
            } catch (IllegalStateException e) {
                // Gérer l'exception si une personne a un dossier médical mais que la date de naissance est manquante
                System.err.println("Erreur lors du calcul de l'âge pour " + person.getFirstName() + " " + person.getLastName() + ": " + e.getMessage());
            }
        }

        return children;
    }





    // Méthode pour récupérer les informations d'une personne
    public List<PersonInfoDTO> getPersonInfo(String firstName, String lastName) {
        // Recherche de la personne par prénom et nom
        Person person = data.getPersons().stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(firstName))
                .filter(p -> p.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElseGet(() -> new Person());  // Retourne une nouvelle Person si non trouvée

        // Récupération du dossier médical via le service MedicalRecordService
        MedicalRecord medicalRecord = data.getMedicalrecords().stream()
                .filter(m -> m.getFirstName().equalsIgnoreCase(firstName))
                .filter(m -> m.getLastName().equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);

        if (medicalRecord == null) {
            // Retourner une liste vide ou une erreur si le MedicalRecord n'est pas trouvé
            return new ArrayList<>();
        }

        // Récupération de la date de naissance et calcul de l'âge
        String birthDateString = medicalRecord.getBirthDate();
        int age = AgeCalculatorDTO.calculateAge(birthDateString);
        // Création du DTO avec les informations de la personne et ses informations médicales
        PersonInfoDTO personInfoDTO = new PersonInfoDTO();
        personInfoDTO.setFirstName(person.getFirstName());
        personInfoDTO.setLastName(person.getLastName());
        personInfoDTO.setAddress(person.getAddress());
        personInfoDTO.setAge(age);
        personInfoDTO.setEmail(person.getEmail());

        // Si le dossier médical est trouvé, on ajoute les médications et allergies, sinon on laisse vides
        if (medicalRecord != null) {
            personInfoDTO.setMedications(medicalRecord.getMedications());
            personInfoDTO.setAllergies(medicalRecord.getAllergies());
        } else {
            personInfoDTO.setMedications(List.of()); // Liste vide si dossier médical non trouvé
            personInfoDTO.setAllergies(List.of()); // Liste vide si dossier médical non trouvé
        }

        // Retourne le DTO avec toutes les informations
        return List.of(personInfoDTO);
    }



}
    //        return data.getPersons().stream()
//                .filter(person -> person.getFirstName().equalsIgnoreCase(firstName)
//                        && person.getLastName().equalsIgnoreCase(lastName))
//                .map(person -> {
//                    int age = calculateAge(person);
//
//                    // Récupération des informations médicales via le service
//                    MedicalRecord record = medicalRecordService.getMedicalRecordForPerson(person);
//                    List<String> medications = record != null ? record.getMedications() : List.of();
//                    List<String> allergies = record != null ? record.getAllergies() : List.of();
//
//                    // Construction du DTO
//                    PersonInfoDTO dto = new PersonInfoDTO();
//                    dto.setFirstName(person.getFirstName());
//                    dto.setLastName(person.getLastName());
//                    dto.setAddress(person.getAddress());
//                    dto.setAge(age);
//                    dto.setEmail(person.getEmail());
//                    dto.setMedications(medications);
//                    dto.setAllergies(allergies);
//
//                    return dto;
//                })
//                .collect(Collectors.toList());





