package com.SmallTalk.model;

import com.SmallTalk.model.Location.LocationTag;

import java.sql.Timestamp;

public class SanFranciscoTag extends LocationTag {

    public SanFranciscoTag(String facebookId, String locality, Timestamp timestamp) {
        super(facebookId, locality, timestamp);
    }
}
