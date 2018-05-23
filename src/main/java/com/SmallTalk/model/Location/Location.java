package com.SmallTalk.model.Location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.web.bind.annotation.RequestMapping;

@JsonIgnoreProperties( ignoreUnknown = true )
public class Location {

    private long latitude;
    private long longitutde;
    private String city;
    private String country;
    private String locality;

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
