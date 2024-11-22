package SafetyNet.alerts.repositorys;

import SafetyNet.alerts.models.Person;
import java.util.List;
import java.util.Optional;

public interface PersonRepository {
    List<Person> findAll();

    Optional<Person> findByEmail(String email);

    List<Person> findByAddress(List<String> addresses);

    List<String> findPhonesByAddresses(List<String> addresses);

    List<String> getAllEmails();
}
