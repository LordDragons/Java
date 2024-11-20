package SafetyNet.alerts.controllers;

import SafetyNet.alerts.dto.AgeCalculatorDTO;
import SafetyNet.alerts.dto.FirestationDTO;
import SafetyNet.alerts.dto.HouseDTO;
import SafetyNet.alerts.models.Data;
import SafetyNet.alerts.models.Firestation;
import SafetyNet.alerts.models.MedicalRecord;
import SafetyNet.alerts.models.Person;
import SafetyNet.alerts.services.FirestationService;
import SafetyNet.alerts.services.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static SafetyNet.alerts.dto.AgeCalculatorDTO.calculateAge;

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



}

