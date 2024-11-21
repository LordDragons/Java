package SafetyNet.alerts.controllers;




import SafetyNet.alerts.dto.HouseDTO;
import SafetyNet.alerts.models.MedicalRecord;
import SafetyNet.alerts.services.FirestationService;
import SafetyNet.alerts.services.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("api/v1/flood")
    public class MedicalRecordController {

        private final FirestationService firestationService;
        private final MedicalRecordService medicalRecordService;

        @Autowired
        public MedicalRecordController(FirestationService firestationService, MedicalRecordService medicalRecordService ) {
            this.firestationService = firestationService;
            this.medicalRecordService = medicalRecordService;
        }

        @GetMapping("/station")
        public ResponseEntity<List<HouseDTO>> getFloodStations(@RequestParam List<Integer> stations) {
            List<HouseDTO> households = firestationService.getHouseholdsByStations(stations);

            if (households.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(households);
        }

        @PostMapping
        public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
            MedicalRecord createdRecord = medicalRecordService.addMedicalRecord(medicalRecord);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRecord);
        }

        @PutMapping
        public ResponseEntity<MedicalRecord> updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
            MedicalRecord updatedRecord = medicalRecordService.updateMedicalRecord(medicalRecord);
            if (updatedRecord == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedRecord);
        }

        @DeleteMapping
        public ResponseEntity<Void> deleteMedicalRecord(@RequestParam String firstName, @RequestParam String lastName) {
            boolean deleted = medicalRecordService.deleteMedicalRecord(firstName, lastName);
            if (!deleted) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.noContent().build();
        }
    }

