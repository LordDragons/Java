package SafetyNet.alerts.dto;


import java.util.List;

public class FirestationDTO {
    private String address;
    private Integer station;
    private List<ResidentInfo> residents;

    public FirestationDTO(String address, Integer station, List<ResidentInfo> residents) {
        this.address = address;
        this.station = station;
        this.residents = residents;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStation() {
        return station;
    }

    public void setStation(Integer station) {
        this.station = station;
    }

    public List<ResidentInfo> getResidents() {
        return residents;
    }

    public void setResidents(List<ResidentInfo> residents) {
        this.residents = residents;
    }

    public static class ResidentInfo {
        private String name;
        private String phone;
        private int age;
        private List<String> medications;
        private List<String> allergies;

        public ResidentInfo(String name, String phone, int age, List<String> medications, List<String> allergies) {
            this.name = name;
            this.phone = phone;
            this.age = age;
            this.medications = medications;
            this.allergies = allergies;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public List<String> getMedications() {
            return medications;
        }

        public void setMedications(List<String> medications) {
            this.medications = medications;
        }

        public List<String> getAllergies() {
            return allergies;
        }

        public void setAllergies(List<String> allergies) {
            this.allergies = allergies;
        }
    }
}
