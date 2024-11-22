package SafetyNet.alerts.controllers;


import SafetyNet.alerts.dto.FirestationDTO;
import SafetyNet.alerts.dto.HouseDTO;
import SafetyNet.alerts.models.Data;
import SafetyNet.alerts.models.Firestation;
import SafetyNet.alerts.services.FirestationService;
import SafetyNet.alerts.services.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1/phoneAlert")
public class FirestationController {

    private final FirestationService firestationService;
    private final MedicalRecordService medicalRecordService;
    private final Data data;

    @Autowired
    public FirestationController(FirestationService firestationService , MedicalRecordService medicalRecordService, Data data) {
        this.firestationService = firestationService;
        this.medicalRecordService = medicalRecordService;
        this.data = data;
    }

    @GetMapping("/station/{stationNumber}")
    public ResponseEntity<List<String>> getPhoneAlert(@PathVariable int stationNumber) {
        List<String> phoneNumbers = firestationService.getPhonesByFirestation(stationNumber);
        if (phoneNumbers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(phoneNumbers);
    }


    @GetMapping("/flood/stations")
    public ResponseEntity<List<HouseDTO>> getHouseholdsByStations(@RequestParam List<Integer> stations) {
        List<HouseDTO> households = firestationService.getHouseholdsByStations(stations);
        if (households.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(households);
    }


    @GetMapping("/fire")
    public ResponseEntity<FirestationDTO> getFireDetails(@RequestParam String address) {
        System.out.println("Adresse reçue dans la requête : " + address);


        FirestationDTO firestationDTO = firestationService.getFireDetails(address);

        if (firestationDTO == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(firestationDTO);

    }

    @GetMapping("/firestation")
    public ResponseEntity<FirestationDTO> getPersonsCoveredByStation(@RequestParam int stationNumber) {
        FirestationDTO firestationDTO = firestationService.getPersonsCoveredByStation(stationNumber);
        if (firestationDTO == null || firestationDTO.getResidents().isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(firestationDTO);
    }


    @PostMapping
    public ResponseEntity<Firestation> addFirestation(@RequestBody Firestation firestation) {
        Firestation createdFirestation = firestationService.addFirestation(firestation);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFirestation);
    }

    // Mettre à jour le numéro de la caserne pour une adresse donnée
    @PutMapping
    public ResponseEntity<Firestation> updateFirestation(@RequestParam String address, @RequestParam Integer station) {
        Firestation updatedFirestation = firestationService.updateFirestation(address, station);
        if (updatedFirestation == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedFirestation);
    }

    // Supprimer un mapping caserne/adresse
    @DeleteMapping
    public ResponseEntity<Void> deleteFirestation(@RequestParam String address) {
        boolean deleted = firestationService.deleteFirestation(address);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

}

