package SafetyNet.alerts.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Firestation {
    private String address;
    private Integer station;

    // Constructor annotated with @JsonCreator
    @JsonCreator
    public Firestation(@JsonProperty("address") String address,
                       @JsonProperty("station") Integer station) {
        this.address = address;
        this.station = station;
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

    @Override
    public String toString() {
        return "firestation{" +
                "address=" + address +
                "station=" + station + '}';
    }

}
