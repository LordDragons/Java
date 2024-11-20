package SafetyNet.alerts.repositorys;

import SafetyNet.alerts.models.Data;
import SafetyNet.alerts.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PersonRepositoryImpl implements PersonRepository {

    private final Data data;

    @Autowired
    public PersonRepositoryImpl(Data data) {
        this.data = data;
    }

    @Override
    public List<String> findPhonesByAddresses(List<String> addresses) {
        return data.getPersons().stream()
                .filter(person -> addresses.contains(person.getAddress())) // Assurez-vous que l'adresse est correcte
                .map(Person::getPhone)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Person> findAll() {
        return data.getPersons();
    }

    @Override
    public Optional<Person> findByEmail(String email) {
        return data.getPersons().stream()
                .filter(person -> person.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public List<Person> findByAddress(List<String> addresses) {
        return data.getPersons().stream()
                .filter(person -> addresses.contains(person.getAddress()))
                .collect(Collectors.toList());
    }
}
