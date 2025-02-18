package SafetyNet.alerts.modelsTests;



import SafetyNet.alerts.models.MedicalRecord;
import SafetyNet.alerts.models.Person;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PersonTest {

    @Test
    public void testDefaultConstructor() {
        // Arrange & Act
        Person person = new Person();

        // Assert
        assertNotNull(person);
        assertNull(person.getFirstName());
        assertNull(person.getLastName());
        assertNull(person.getAddress());
        assertNull(person.getCity());
        assertNull(person.getZip());
        assertNull(person.getPhone());
        assertNull(person.getEmail());
        assertNull(person.getMedicalRecord());
    }

    @Test
    public void testParameterizedConstructor() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        String address = "123 Main St";
        String city = "SomeCity";
        String zip = "12345";
        String phone = "555-5555";
        String email = "john.doe@example.com";

        // Act
        Person person = new Person(firstName, lastName, address, city, zip, phone, email);

        // Assert
        assertEquals(firstName, person.getFirstName());
        assertEquals(lastName, person.getLastName());
        assertEquals(address, person.getAddress());
        assertEquals(city, person.getCity());
        assertEquals(zip, person.getZip());
        assertEquals(phone, person.getPhone());
        assertEquals(email, person.getEmail());
    }

    @Test
    public void testSettersAndGetters() {
        // Arrange
        Person person = new Person();
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/1990", Arrays.asList("Med1"), Arrays.asList("Pollen"));

        // Act
        person.setFirstName("Jane");
        person.setLastName("Smith");
        person.setAddress("456 Elm St");
        person.setCity("NewCity");
        person.setZip("67890");
        person.setPhone("555-1234");
        person.setEmail("jane.smith@example.com");
        person.setMedicalRecord(medicalRecord);

        // Assert
        assertEquals("Jane", person.getFirstName());
        assertEquals("Smith", person.getLastName());
        assertEquals("456 Elm St", person.getAddress());
        assertEquals("NewCity", person.getCity());
        assertEquals("67890", person.getZip());
        assertEquals("555-1234", person.getPhone());
        assertEquals("jane.smith@example.com", person.getEmail());
        assertEquals(medicalRecord, person.getMedicalRecord());
    }

    @Test
    public void testCalculateAge() {
        // Arrange
        Person person = new Person();
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/1990", Arrays.asList("Med1"), Arrays.asList("Pollen"));
        person.setMedicalRecord(medicalRecord);

        // Act
        int age = person.calculateAge();

        // Assert
        assertEquals(35, age); // Adjust this value based on the current year and birthdate.
    }

    @Test
    public void testCalculateAgeWithMissingMedicalRecord() {
        // Arrange
        Person person = new Person();

        // Act & Assert
        Exception exception = assertThrows(IllegalStateException.class, person::calculateAge);
        assertEquals("Le dossier médical ou la date de naissance est manquant", exception.getMessage());
    }

    @Test
    public void testToString() {
        // Arrange
        Person person = new Person("John", "Doe", "123 Main St", "SomeCity", "12345", "555-5555", "john.doe@example.com");
        MedicalRecord medicalRecord = new MedicalRecord("John", "Doe", "01/01/1990", Arrays.asList("Med1"), Arrays.asList("Pollen"));
        person.setMedicalRecord(medicalRecord);

        // Act
        String result = person.toString();

        // Assert
        String expected = "Person{firstName='John', lastName='Doe', address='123 Main St', city='SomeCity', zip='12345', phone='555-5555', email='john.doe@example.com', medicalRecord=MedicalRecord{firstName='John', lastName='Doe', birthdate='01/01/1990', medications=[Med1], allergies=[Pollen]}}";
        assertEquals(expected, result);
    }

    @Test
    public void testToStringWithoutMedicalRecord() {
        // Arrange
        Person person = new Person("John", "Doe", "123 Main St", "SomeCity", "12345", "555-5555", "john.doe@example.com");

        // Act
        String result = person.toString();

        // Assert
        String expected = "Person{firstName='John', lastName='Doe', address='123 Main St', city='SomeCity', zip='12345', phone='555-5555', email='john.doe@example.com', medicalRecord=Dossier médical manquant}";
        assertEquals(expected, result);
    }
}

