package com.SmallTalk.model;

import com.SmallTalk.model.Location.LocationTag;

import java.sql.Timestamp;

public class SanFranciscoTag extends LocationTag {

    public SanFranciscoTag(String username, String locality, Timestamp timestamp, Double longitude, Double latitude) {
        super(username, locality, latitude, longitude, timestamp);
    }
}
