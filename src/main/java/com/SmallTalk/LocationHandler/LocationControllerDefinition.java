package com.SmallTalk.LocationHandler;

import com.SmallTalk.model.User.User;

public interface LocationControllerDefinition {

    public void trackLocation(Double longitude,
                              Double latitude,
                              int zipCode,
                              User user);
}
