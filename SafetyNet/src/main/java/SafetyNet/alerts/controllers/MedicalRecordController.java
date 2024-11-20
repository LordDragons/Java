package SafetyNet.alerts.controllers;




import SafetyNet.alerts.dto.HouseDTO;
import SafetyNet.alerts.services.FirestationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

    @RestController
    @RequestMapping("api/v1/flood")
    public class MedicalRecordController {

        private final FirestationService firestationService;

        @Autowired
        public MedicalRecordController(FirestationService firestationService) {
            this.firestationService = firestationService;
        }

        @GetMapping("/station")
        public ResponseEntity<List<HouseDTO>> getFloodStations(@RequestParam List<Integer> stations) {
            List<HouseDTO> households = firestationService.getHouseholdsByStations(stations);

            if (households.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(households);
        }
    }

