package com.SmallTalk.model;

import java.sql.Timestamp;

public abstract class LocationTag {

    String facebookId;
    String locality;
    Timestamp timestamp;

    public LocationTag(String facebookId, String locality, Timestamp timestamp) {
        this.facebookId = facebookId;
        this.locality = locality;
        this.timestamp = timestamp;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
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
