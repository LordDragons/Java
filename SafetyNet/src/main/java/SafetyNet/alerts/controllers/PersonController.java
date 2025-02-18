package SafetyNet.alerts.controllers;


import SafetyNet.alerts.dto.PersonInfoDTO;
import SafetyNet.alerts.dto.ChildAlertDTO;
import SafetyNet.alerts.models.Person;
import SafetyNet.alerts.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/persons")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/emails")
    public List<String> getEmailsList() {
        return personService.getEmailsList();
    }

    @GetMapping("/childAlert")
    public ResponseEntity<List<ChildAlertDTO>> getChildrenAlert(@RequestParam String address) {
        // Récupère les enfants à l'adresse donnée
        List<ChildAlertDTO> children = personService.getChildrenByAddress(address);

        // Si aucun enfant n'est trouvé, retourner une chaîne vide
        if (children.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(children);
    }

    @GetMapping("/personInfo")
    public ResponseEntity<List<PersonInfoDTO>> getPersonInfo(
            @RequestParam String firstName,
            @RequestParam String lastName) {

        // Appelle le service pour obtenir les informations des personnes
        List<PersonInfoDTO> personInfos = personService.getPersonInfo(firstName, lastName);

        // Si aucune personne n'est trouvée, retourne une réponse vide
        if (personInfos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Sinon, retourne les informations des personnes
        return ResponseEntity.ok(personInfos);
    }

    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        Person createdPerson = personService.addPerson(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPerson);
    }

    @PutMapping
    public ResponseEntity<Optional<Person>> updatePerson(@RequestParam String firstName,
                                                         @RequestParam String lastName,
                                                         @RequestParam String address,
                                                         @RequestParam String city,
                                                         @RequestParam String zip,
                                                         @RequestParam String phone,
                                                         @RequestParam String email) {
        // Création d’un objet Person à partir des paramètres de requête
        Person personToUpdate = new Person(firstName, lastName, address, city, zip, phone, email);

        // Tentative de mise à jour de la personne
        Optional<Person> updatedPerson = personService.updatePerson(personToUpdate);

        // Vérifier si la mise à jour a réussi
        if (updatedPerson.isPresent()) {
            return ResponseEntity.ok(updatedPerson);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePerson(@RequestParam String firstName,
                                             @RequestParam String lastName) {
        if (firstName == null || firstName.isEmpty() ||
                lastName == null || lastName.isEmpty()) {
            return ResponseEntity.badRequest().build(); // Return 400 Bad Request for invalid input
        }

        boolean deleted = personService.deletePerson(firstName, lastName);

        if (!deleted) {
            return ResponseEntity.notFound().build(); // Return 404 if no person was found to delete
        }

        return ResponseEntity.noContent().build(); // Return 204 if the person was deleted
    }

}
