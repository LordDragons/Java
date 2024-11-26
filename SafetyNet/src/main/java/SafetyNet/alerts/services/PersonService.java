package SafetyNet.alerts.services;


import SafetyNet.alerts.dto.ChildAlertDTO;
import SafetyNet.alerts.dto.PersonInfoDTO;
import SafetyNet.alerts.models.Data;
import SafetyNet.alerts.models.MedicalRecord;
import SafetyNet.alerts.models.Person;
import SafetyNet.alerts.repositorys.MedicalRecordRepositoryImpl;
import SafetyNet.alerts.repositorys.PersonRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;



@Service
public class PersonService {


    private final MedicalRecordService medicalRecordService;  // Injection de dépendance
    private final PersonRepositoryImpl personRepositoryImpl;
    private final MedicalRecordRepositoryImpl medicalRecordRepositoryImpl;




    // Constructeur avec injection de MedicalRecordService
    @Autowired
    public PersonService(MedicalRecordService medicalRecordService, PersonRepositoryImpl personRepositoryImpl,MedicalRecordRepositoryImpl medicalRecordRepositoryImpl)
    {
        this.medicalRecordService = medicalRecordService;  // Assignation de la dépendance
        this.personRepositoryImpl = personRepositoryImpl;
        this.medicalRecordRepositoryImpl = medicalRecordRepositoryImpl;
    }

    // Méthode pour récupérer la liste des emails
    public List<String> getEmailsList() {
        return personRepositoryImpl.getAllEmails();
    }

    // Méthode pour récupérer les enfants d'une adresse
    public List<ChildAlertDTO> getChildrenByAddress(String address) {
        // Fetch all persons at the given address
        List<Person> personsAtAddress = personRepositoryImpl.findByAddress(Collections.singletonList(address));
        List<ChildAlertDTO> children = new ArrayList<>();

        for (Person person : personsAtAddress) {
            try {
                // Assume this returns a List<MedicalRecord>
                List<MedicalRecord> medicalRecords = medicalRecordRepositoryImpl.findByFirstNameAndLastName(
                        person.getFirstName(),
                        person.getLastName()
                );

                // Ensure there's at least one medical record
                if (medicalRecords == null || medicalRecords.isEmpty()) {
                    System.err.println("No medical record found for " + person.getFirstName() + " " + person.getLastName());
                    continue;
                }

                // Use the first record for this person (if multiple exist)
                MedicalRecord medicalRecord = medicalRecords.get(0);

                String birthDateString = medicalRecord.getBirthDate();
                int age = calculateAge(birthDateString);

                if (age <= 18) {
                    // Collect household members excluding the child
                    List<String> householdMembers = new ArrayList<>();
                    for (Person householdMember : personsAtAddress) {
                        if (!householdMember.equals(person)) {
                            householdMembers.add(householdMember.getFirstName() + " " + householdMember.getLastName());
                        }
                    }

                    // Create ChildAlertDTO
                    ChildAlertDTO child = new ChildAlertDTO(
                            person.getFirstName(),
                            person.getLastName(),
                            age,
                            householdMembers
                    );
                    children.add(child);
                }
            } catch (IllegalStateException e) {
                System.err.println("Error processing medical record for " + person.getFirstName() + " " + person.getLastName() + ": " + e.getMessage());
            }
        }

        return children;
    }



    // Méthode pour récupérer les informations d'une personne
    public List<PersonInfoDTO> getPersonInfo(String firstName, String lastName) {
        // Retourne le DTO avec toutes les informations
        return personRepositoryImpl.getPersonInfo(firstName, lastName);
    }

    public Person addPerson(Person person) {
        personRepositoryImpl.getPersons().add(person);
        return person;
    }


    public Optional<Person> updatePerson(Person person) {
        return personRepositoryImpl.getPersons().stream()
                .filter(p -> p.getFirstName().equalsIgnoreCase(person.getFirstName()) &&
                        p.getLastName().equalsIgnoreCase(person.getLastName()))
                .findFirst()
                .map(existingPerson -> {
                    existingPerson.setAddress(person.getAddress());
                    existingPerson.setCity(person.getCity());
                    existingPerson.setZip(person.getZip());
                    existingPerson.setPhone(person.getPhone());
                    existingPerson.setEmail(person.getEmail());
                    System.out.println("Updated person: " + existingPerson);
                    return existingPerson;
                });
    }


    public boolean deletePerson(String firstName, String lastName) {
        boolean isDeleted = personRepositoryImpl.getPersons().removeIf(p ->
                p.getFirstName().equalsIgnoreCase(firstName) &&
                        p.getLastName().equalsIgnoreCase(lastName));

        if (isDeleted) {
            System.out.println("Person deleted: " + firstName + " " + lastName);
        } else {
            System.out.println("Person not found: " + firstName + " " + lastName);
        }
        return isDeleted;
    }

    public static int calculateAge(String birthDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        try {
            LocalDate birthDate = LocalDate.parse(birthDateString, formatter);

            if (birthDate.isAfter(LocalDate.now())) {
                System.err.println("La date de naissance ne peut pas être dans le futur : " + birthDateString);
                return 0; // Returning 0 or some default value
            }

            LocalDate currentDate = LocalDate.now();
            return Period.between(birthDate, currentDate).getYears();
        } catch (DateTimeParseException e) {
            System.err.println("Format de date invalide : " + birthDateString);
            return 0; // Handle parsing error by returning 0
        }


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