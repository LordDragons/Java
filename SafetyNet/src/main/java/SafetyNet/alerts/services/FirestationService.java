package SafetyNet.alerts.services;


import SafetyNet.alerts.dto.FirestationDTO;
import SafetyNet.alerts.dto.HouseDTO;
import SafetyNet.alerts.models.Data;
import SafetyNet.alerts.models.Firestation;
import SafetyNet.alerts.models.MedicalRecord;
import SafetyNet.alerts.models.Person;
import SafetyNet.alerts.repositorys.FirestationRepository;
import SafetyNet.alerts.repositorys.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FirestationService {

    private final Data data;
    private final FirestationRepository firestationRepository;
    private final PersonRepository personRepository;

    @Autowired
    public FirestationService(Data data, FirestationRepository firestationRepository, PersonRepository personRepository) {
        this.data = data;
        this.firestationRepository = firestationRepository;
        this.personRepository = personRepository;
    }

    // Méthode pour récupérer les numéros de téléphone par station
    public List<String> getPhonesByFirestation(int station) {
        List<String> addresses = firestationRepository.findAddressesByStationNumber(station);
        if (addresses.isEmpty()) {
            return new ArrayList<>();
        }
        return personRepository.findPhonesByAddresses(addresses);
    }

    // Méthode pour récupérer les foyers par liste de stations
    public List<HouseDTO> getHouseholdsByStations(List<Integer> stations) {
        return stations.stream()
                .flatMap(station -> firestationRepository.findAddressesByStationNumber(station).stream()
                        .map(address -> createHouseDTO(address)))
                .collect(Collectors.toList());
    }

    private HouseDTO createHouseDTO(String address) {
        List<HouseDTO.Resident> residents = personRepository.findByAddress(List.of(address)).stream()
                .map(this::createResidentFromPerson)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new HouseDTO(address, residents);
    }

    public HouseDTO.Resident createResidentFromPerson(Person person) {
        MedicalRecord medicalRecord = data.getMedicalrecords().stream()
                .filter(m -> m.getFirstName().equalsIgnoreCase(person.getFirstName()))
                .filter(m -> m.getLastName().equalsIgnoreCase(person.getLastName()))
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
        // Récupérer les adresses liées à la station
        List<String> addresses = firestationRepository.findAddressesByStationNumber(stationNumber);

        if (addresses.isEmpty()) {
            return new FirestationDTO(null, stationNumber, 0L, 0L, new ArrayList<>());
        }

        // Récupérer les résidents pour chaque adresse
        List<FirestationDTO.ResidentInfo> residents = new ArrayList<>();
        long numberOfChildren = 0;
        long numberOfAdults = 0;


        for (String address : addresses) {
            List<Person> persons = personRepository.findByAddress(List.of(address));

            for (Person person : persons) {
                MedicalRecord medicalRecord = data.getMedicalrecords().stream()
                        .filter(m -> m.getFirstName().equalsIgnoreCase(person.getFirstName()))
                        .filter(m -> m.getLastName().equalsIgnoreCase(person.getLastName()))
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
                } else {
                    System.out.println("No MedicalRecord found for: " + person.getFirstName() + " " + person.getLastName());
                }
            }
        }

        // Créer et retourner l'objet FirestationDTO avec les nouvelles valeurs
        return new FirestationDTO(
                addresses.isEmpty() ? null : String.join(", ", addresses),
                stationNumber,
                numberOfAdults,
                numberOfChildren,
                residents
        );
    }



    public FirestationDTO getFireDetails(String address) {
        List<Person> peopleAtAddress = data.getPersons().stream()
                .filter(person -> person.getAddress().equalsIgnoreCase(address))
                .collect(Collectors.toList());

        Firestation firestation = data.getFirestations().stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase(address))
                .findFirst()
                .orElse(null);

        if (firestation == null || peopleAtAddress.isEmpty()) {
            return null;
        }

        List<FirestationDTO.ResidentInfo> residents = new ArrayList<>();
        long numberOfChildren = 0;
        long numberOfAdults = 0;

        // Parcours des personnes à l'adresse pour calculer les enfants et adultes
        for (Person person : peopleAtAddress) {
            MedicalRecord medicalRecord = data.getMedicalrecords().stream()
                    .filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName()) &&
                            record.getLastName().equalsIgnoreCase(person.getLastName()))
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

        // Créer et retourner l'objet FirestationDTO avec les compteurs d'adultes et d'enfants
        return new FirestationDTO(
                firestation.getAddress(),
                firestation.getStation(),
                numberOfAdults,
                numberOfChildren,
                residents
        );
    }


    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    // Ajouter un nouveau mapping caserne/adresse
    public Firestation addFirestation(Firestation firestation) {
        data.getFirestations().add(firestation);
        return firestation;
    }


    // Mettre à jour le numéro de la caserne pour une adresse donnée
    public Firestation updateFirestation(String address, Integer newStation) {
        return data.getFirestations().stream()
                .filter(fs -> fs.getAddress().equalsIgnoreCase(address))
                .findFirst()
                .map(existingFirestation -> {
                    existingFirestation.setStation(newStation);
                    return existingFirestation;
                }).orElse(null);
    }

    // Supprimer un mapping caserne/adresse
    public boolean deleteFirestation(String address) {
        return data.getFirestations().removeIf(fs -> fs.getAddress().equalsIgnoreCase(address));
    }

}
