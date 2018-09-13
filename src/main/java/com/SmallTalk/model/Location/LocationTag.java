package com.SmallTalk.model.Location;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "sanfrancisco")
public class LocationTag {

    @Column
    String username;
    @Column
    String locality;
    @Column
    Double longitude;
    @Column
    Double latitude;
    @Id
    Timestamp timestamp;

    public LocationTag() {}

    public LocationTag(String username, String locality, Double longitude, Double latitude, Timestamp timestamp) {
        this.username = username;
        this.locality = locality;
        this.timestamp = timestamp;
        this.longitude = longitude;
        this.latitude = latitude;
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


}
