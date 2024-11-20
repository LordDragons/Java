package SafetyNet.alerts.controllers;


import SafetyNet.alerts.dto.PersonInfoDTO;
import SafetyNet.alerts.dto.ChildAlertDTO;
import SafetyNet.alerts.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

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

        // Sinon, retourner les enfants avec un code 200
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
}
