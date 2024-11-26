package SafetyNet.alerts.services;

import SafetyNet.alerts.dto.FirestationDTO;
import SafetyNet.alerts.dto.HouseDTO;
import SafetyNet.alerts.models.Firestation;
import SafetyNet.alerts.models.MedicalRecord;
import SafetyNet.alerts.models.Person;
import SafetyNet.alerts.repositorys.FirestationRepository;
import SafetyNet.alerts.repositorys.FirestationRepositoryImpl;
import SafetyNet.alerts.repositorys.MedicalRecordRepositoryImpl;
import SafetyNet.alerts.repositorys.PersonRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class FirestationService {

    private final FirestationRepository firestationRepository;
    private final PersonRepositoryImpl personRepositoryImpl;
    private final MedicalRecordRepositoryImpl medicalRecordRepositoryImpl;
    private final FirestationRepositoryImpl firestationRepositoryImpl;



    @Autowired
    public FirestationService(FirestationRepository firestationRepository,
                              PersonRepositoryImpl personRepositoryImpl,
                              MedicalRecordRepositoryImpl medicalRecordRepositoryImpl,
                              FirestationRepositoryImpl firestationRepositoryImpl) {
        this.firestationRepository = firestationRepository;
        this.personRepositoryImpl = personRepositoryImpl;
        this.medicalRecordRepositoryImpl = medicalRecordRepositoryImpl;
        this.firestationRepositoryImpl = firestationRepositoryImpl;
    }

    public List<Firestation> getFirestations() {
        return firestationRepository.getFirestations();
    }

    public List<String> getPhonesByFirestation(int station) {
        List<String> addresses = firestationRepository.findAddressesByStationNumber(station);
        if (addresses.isEmpty()) {
            return new ArrayList<>();
        }
        return personRepositoryImpl.findPhonesByAddresses(addresses);
    }

    public List<HouseDTO> getHouseholdsByStations(List<Integer> stations) {
        return stations.stream()
                .flatMap(station -> firestationRepository.findAddressesByStationNumber(station).stream()
                        .map(address -> createHouseDTO(address)))
                .collect(Collectors.toList());
    }

    private HouseDTO createHouseDTO(String address) {
        List<HouseDTO.Resident> residents = personRepositoryImpl.findByAddress(List.of(address)).stream()
                .map(this::createResidentFromPerson)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new HouseDTO(address, residents);
    }

    public HouseDTO.Resident createResidentFromPerson(Person person) {
        MedicalRecord medicalRecord = medicalRecordRepositoryImpl.findByFirstNameAndLastName(
                        person.getFirstName(), person.getLastName()).stream()
                .findFirst()
                .orElse(null);

        if (medicalRecord == null) {
            logMissingMedicalRecord(person);
            return null;
        }

        return new HouseDTO.Resident(
                person.getFirstName(),
                person.getLastName(),
                person.getPhone(),
                PersonService.calculateAge(medicalRecord.getBirthDate()),
                medicalRecord.getMedications(),
                medicalRecord.getAllergies()
        );
    }

    public void logMissingMedicalRecord(Person person) {
        System.err.println("Aucun MedicalRecord trouvé pour la personne : "
                + person.getFirstName() + " " + person.getLastName());
    }

    public FirestationDTO getPersonsCoveredByStation(int stationNumber) {
        List<String> addresses = firestationRepository.findAddressesByStationNumber(stationNumber);

        if (addresses.isEmpty()) {
            return new FirestationDTO(null, stationNumber, 0L, 0L, new ArrayList<>());
        }

        List<FirestationDTO.ResidentInfo> residents = new ArrayList<>();
        long numberOfChildren = 0;
        long numberOfAdults = 0;

        for (String address : addresses) {
            List<Person> persons = personRepositoryImpl.findByAddress(List.of(address));

            for (Person person : persons) {
                MedicalRecord medicalRecord = medicalRecordRepositoryImpl.findByFirstNameAndLastName(
                                person.getFirstName(), person.getLastName()).stream()
                        .findFirst()
                        .orElse(null);

                if (medicalRecord != null) {
                    int age = PersonService.calculateAge(medicalRecord.getBirthDate());

                    if (age <= 18) {
                        numberOfChildren++;
                    } else {
                        numberOfAdults++;
                    }

                    residents.add(new FirestationDTO.ResidentInfo(
                            person.getFirstName() + " " + person.getLastName(),
                            person.getPhone(),
                            age,
                            medicalRecord.getMedications(),
                            medicalRecord.getAllergies()
                    ));
                }
            }
        }

        return new FirestationDTO(
                addresses.isEmpty() ? null : String.join(", ", addresses),
                stationNumber,
                numberOfAdults,
                numberOfChildren,
                residents
        );
    }

    public FirestationDTO getFireDetails(String address) {
        List<Firestation> firestations = firestationRepository.findByAddress(Collections.singletonList(address));
        if (!firestations.isEmpty()) {
            Firestation firestation = firestations.get(0);  // Get the first firestation if available
            List<Person> peopleAtAddress = personRepositoryImpl.findByAddress(List.of(address));

            System.out.println("Personnes à l'adresse : " + peopleAtAddress);
            if (peopleAtAddress.isEmpty()) {
                System.out.println("Aucune personne trouvée à l'adresse : " + address);
                return null;
            }

            List<FirestationDTO.ResidentInfo> residents = new ArrayList<>();
            long numberOfChildren = 0;
            long numberOfAdults = 0;

            for (Person person : peopleAtAddress) {
                MedicalRecord medicalRecord = medicalRecordRepositoryImpl.findByFirstNameAndLastName(
                                person.getFirstName(), person.getLastName()).stream()
                        .findFirst()
                        .orElse(null);

                if (medicalRecord == null) {
                    continue;
                }

                int age = PersonService.calculateAge(medicalRecord.getBirthDate());
                if (age <= 18) {
                    numberOfChildren++;
                } else {
                    numberOfAdults++;
                }

                residents.add(new FirestationDTO.ResidentInfo(
                        person.getFirstName() + " " + person.getLastName(),
                        person.getPhone(),
                        age,
                        medicalRecord.getMedications(),
                        medicalRecord.getAllergies()
                ));
            }

            return new FirestationDTO(
                    firestation.getAddress(),
                    firestation.getStation(),
                    numberOfAdults,
                    numberOfChildren,
                    residents
            );
        } else {
            System.out.println("Aucune firestation trouvée pour l'adresse : " + address);
            return null;  // Return null if no firestation is found
        }
    }



    public Firestation addFirestation(Firestation firestation) {
        firestationRepository.save(firestation);
        return firestation;
    }

    public Firestation updateFirestation(String address, Integer newStation) {
        // Récupérer la liste des firestations correspondant à l'adresse
        List<Firestation> firestations = firestationRepository.findByAddress(Collections.singletonList(address));

        if (!firestations.isEmpty()) {
            // Récupérer le premier élément de la liste
            Firestation existingFirestation = firestations.get(0);
            existingFirestation.setStation(newStation); // Mise à jour de la station
            firestationRepository.save(existingFirestation); // Sauvegarde après mise à jour
            return existingFirestation;
        }
        return null; // Retourne null si aucune firestation n'est trouvée
    }



    public boolean deleteFirestation(String address) {
        // Récupérer la liste des firestations correspondant à l'adresse
        List<Firestation> firestations = firestationRepository.findByAddress(Collections.singletonList(address));

        if (!firestations.isEmpty()) {
            Firestation firestation = firestations.get(0); // Récupérer le premier élément de la liste
            firestationRepository.delete(firestation);    // Supprimer l'élément trouvé
            return true;
        }
        return false; // Retourne false si aucune firestation n'est trouvée
    }



}
