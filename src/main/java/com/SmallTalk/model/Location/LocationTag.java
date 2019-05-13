package com.SmallTalk.model.Location;

import com.SmallTalk.model.User.User;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "sanfrancisco")
public class LocationTag {

@Column
private String username;
@Column
private String locality;
@Column
private Double longitude;
@Column
private Double latitude;
@Id
private Timestamp timestamp;
@Column
private int zipcode;

public LocationTag() {}

public LocationTag(String username, String locality, Double longitude, Double latitude, Timestamp timestamp, int zipcode) {
    this.username = username;
    this.locality = locality;
    this.timestamp = timestamp;
    this.longitude = longitude;
    this.latitude = latitude;
    this.zipcode = zipcode;
}

    public Double getLongitude() {
    return longitude;
}

    public void setLongitude(Double longitude) {
    this.longitude = longitude;
}

    public Double getLatitude() {
    return latitude;
}

    public void setLatitude(Double latitude) {
    this.latitude = latitude;
}

    public String getUsername() {
    return username;
}

    public void setUsername(String username) {
    this.username = username;
}

    public String getLocality() {
    return locality;
}

    public void setLocality(String locality) {
    this.locality = locality;
}

    public Timestamp getTimestamp() {
    return timestamp;
}

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getZipCode() {
        return this.zipcode;
    }

    public void setZipCode(int zipCode) { this.zipcode = zipCode;
    }




}
