package com.SmallTalk.LocationHandler;

import com.SmallTalk.model.Location.LocationTag;
import com.SmallTalk.model.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public void trackLocation(User user, Double longitude, Double latitude) {

        LocationTag locationTag
                    = new LocationTag(user.getUserName(),
                    user.getLocality(),
                    longitude,
                    latitude,
                    Timestamp.from(Instant.now()));


        locationRepository.save(locationTag);

    }

    public List pullNearbyUsers() {

        return locationRepository.findAll();

    }

}
