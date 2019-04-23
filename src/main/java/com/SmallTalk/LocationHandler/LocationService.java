package com.SmallTalk.LocationHandler;

import com.SmallTalk.AccountHandler.AccountController;
import com.SmallTalk.model.Location.LocationTag;
import com.SmallTalk.model.User.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    private Logger logger = LoggerFactory.getLogger(LocationService.class);

    public void trackLocation(User user, Double longitude, Double latitude) {

        LocationTag locationTag
                    = new LocationTag(user.getusername(),
                    user.getLocality(),
                    longitude,
                    latitude,
                    Timestamp.from(Instant.now()));

        logger.info("Tracking Location for user: ", user);

        locationRepository.save(locationTag);

    }

    public List pullNearbyUsers() {

        return locationRepository.findAll();

    }

}
