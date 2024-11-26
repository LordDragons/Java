package SafetyNet.alerts.controllers;


import SafetyNet.alerts.dto.FirestationDTO;
import SafetyNet.alerts.dto.HouseDTO;
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

    @Autowired
    public FirestationController(FirestationService firestationService , MedicalRecordService medicalRecordService) {
        this.firestationService = firestationService;
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping("/station/{stationNumber}")
    public ResponseEntity<List<String>> getPhoneAlert(@PathVariable int stationNumber) {
        List<String> phoneNumbers = firestationService.getPhonesByFirestation(stationNumber);
        return phoneNumbers.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(phoneNumbers);
    }

    @GetMapping("/flood/stations")
    public ResponseEntity<List<HouseDTO>> getHouseholdsByStations(@RequestParam List<Integer> stations) {
        List<HouseDTO> households = firestationService.getHouseholdsByStations(stations);
        return households.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(households);
    }

    @GetMapping("/fire")
    public ResponseEntity<FirestationDTO> getFireDetails(@RequestParam String address) {
        FirestationDTO firestationDTO = firestationService.getFireDetails(address);
        if (firestationDTO == null) {
            System.out.println("Aucun détail trouvé pour l'adresse : " + address);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(firestationDTO);
    }


    @GetMapping("/firestation")
    public ResponseEntity<FirestationDTO> getPersonsCoveredByStation(@RequestParam int stationNumber) {
        FirestationDTO firestationDTO = firestationService.getPersonsCoveredByStation(stationNumber);
        return (firestationDTO == null || firestationDTO.getResidents().isEmpty()) ? ResponseEntity.noContent().build() : ResponseEntity.ok(firestationDTO);
    }

    @PostMapping
    public ResponseEntity<Firestation> addFirestation(@RequestBody Firestation firestation) {
        Firestation createdFirestation = firestationService.addFirestation(firestation);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFirestation);
    }

    @PutMapping
    public ResponseEntity<Firestation> updateFirestation(@RequestParam String address, @RequestParam Integer station) {
        Firestation updatedFirestation = firestationService.updateFirestation(address, station);
        return updatedFirestation == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(updatedFirestation);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteFirestation(@RequestParam String address) {
        boolean deleted = firestationService.deleteFirestation(address);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}


