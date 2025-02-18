package SafetyNet.alerts.models;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class Data {

    public Data() {
        System.out.println("✅ Data bean créé !");
    }

    private List<Firestation> firestations = new ArrayList<>();
    private List<MedicalRecord> medicalrecords = new ArrayList<>();
    private List<Person> persons = new ArrayList<>();

    public List<MedicalRecord> getMedicalrecords() {
        return medicalrecords;
    }

    public void setMedicalrecords(List<MedicalRecord> medicalrecords) {
        this.medicalrecords = (medicalrecords != null) ? medicalrecords : new ArrayList<>();
    }

    public List<Firestation> getFirestations() {
        return firestations;
    }

    public void setFirestations(List<Firestation> firestations) {
        this.firestations = (firestations != null) ? firestations : new ArrayList<>();
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = (persons != null) ? persons : new ArrayList<>();
    }

    public void addFirestation(Firestation firestation) {
        this.firestations.add(firestation);
    }

    public void addMedicalRecord(MedicalRecord medicalRecord) {
        this.medicalrecords.add(medicalRecord);
    }

    public void addPerson(Person person) {
        this.persons.add(person);
    }

    @Override
    public String toString() {
        return "Data{" +
                "firestations=" + firestations +
                ", medicalrecords=" + medicalrecords +
                ", persons=" + persons +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Data data = (Data) o;
        return firestations.equals(data.firestations) &&
                medicalrecords.equals(data.medicalrecords) &&
                persons.equals(data.persons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firestations, medicalrecords, persons);
    }
}
