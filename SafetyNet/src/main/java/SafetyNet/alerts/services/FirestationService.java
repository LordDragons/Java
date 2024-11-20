package SafetyNet.alerts.services;

import SafetyNet.alerts.dto.AgeCalculatorDTO;
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

    private HouseDTO.Resident createResidentFromPerson(Person person) {
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
                AgeCalculatorDTO.calculateAge(medicalRecord.getBirthDate()),
                medicalRecord.getMedications(),
                medicalRecord.getAllergies()
        );
    }

    private void logMissingMedicalRecord(Person person) {
        System.err.println("Aucun MedicalRecord trouvé pour la personne : "
                + person.getFirstName() + " " + person.getLastName());
    }
    public FirestationDTO getPersonsCoveredByStation(int stationNumber) {
        // Récupérer les adresses liées à la station
        List<String> addresses = firestationRepository.findAddressesByStationNumber(stationNumber);

        if (addresses.isEmpty()) {
            return new FirestationDTO(null, stationNumber, new ArrayList<>());
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
                    int age = AgeCalculatorDTO.calculateAge(medicalRecord.getBirthDate());
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

        // Créer et retourner l'objet FirestationDTO
        return new FirestationDTO(
                addresses.isEmpty() ? null : String.join(", ", addresses),
                stationNumber,
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

        List<FirestationDTO.ResidentInfo> residents = peopleAtAddress.stream()
                .map(person -> {
                    MedicalRecord medicalRecord = data.getMedicalrecords().stream()
                            .filter(record -> record.getFirstName().equalsIgnoreCase(person.getFirstName()) &&
                                    record.getLastName().equalsIgnoreCase(person.getLastName()))
                            .findFirst()
                            .orElse(null);

                    if (medicalRecord == null) {
                        return null;
                    }

                    String firstName = person.getFirstName() + " " + person.getLastName();
                    String phone = person.getPhone();
                    int age = AgeCalculatorDTO.calculateAge(medicalRecord.getBirthDate());
                    List<String> medications = medicalRecord.getMedications();
                    List<String> allergies = medicalRecord.getAllergies();

                    return new FirestationDTO.ResidentInfo(firstName, phone, age, medications, allergies);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new FirestationDTO(firestation.getAddress(), firestation.getStation(), residents);
    }

    private int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
}