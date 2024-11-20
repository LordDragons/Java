package SafetyNet.alerts.dto;

import java.util.List;

public class ChildAlertDTO {
    private String firstName;
    private String lastName;
    private int age;
    private List<String> householdMembers;

    // Constructor
    public ChildAlertDTO(String firstName, String lastName, int age, List<String> householdMembers) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.householdMembers = householdMembers;
    }

    // Getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<String> getHouseholdMembers() {
        return householdMembers;
    }

    public void setHouseholdMembers(List<String> householdMembers) {
        this.householdMembers = householdMembers;
    }
}
