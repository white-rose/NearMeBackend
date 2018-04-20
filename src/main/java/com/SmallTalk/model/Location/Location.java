package com.SmallTalk.model.Location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class Location {

    private String locality;
    private long latitude;
    private long longitutde;

    public Location() {

    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitutde() {
        return longitutde;
    }

    public void setLongitutde(long longitutde) {
        this.longitutde = longitutde;
    }

}
