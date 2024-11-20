package SafetyNet.alerts.models;

import java.util.List;

public class Data {

    private List<Firestation> firestations;
    private List<MedicalRecord> medicalrecords;
    private List<Person> persons;

    public List<MedicalRecord> getMedicalrecords() {
        return medicalrecords;
    }

    public void setMedicalrecords(List<MedicalRecord> medicalrecords) {
        this.medicalrecords = medicalrecords;
    }

    public List<Firestation> getFirestations() {
        return firestations;
    }

    public void setFirestations(List<Firestation> firestation) {
        this.firestations = firestation;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

}
