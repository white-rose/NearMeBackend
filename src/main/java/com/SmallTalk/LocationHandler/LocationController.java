package com.SmallTalk.LocationHandler;

import com.SmallTalk.model.Location.LocationTag;
import com.SmallTalk.model.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Random;

@RestController
public class LocationController implements LocationCleanser {

    @Autowired
    LocationRepository locationRepository;

    @RequestMapping("/track")
    public void trackLocation(@RequestParam Double longitude,
                              @RequestParam Double latitude,
                              @RequestParam String locality) {

        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));

        LocationTag locationTag
                = new LocationTag(generatedString, locality, Timestamp.from(Instant.now()), longitude, latitude);

        locationRepository.save(locationTag);

    }

    @Override
//    @Scheduled(fixedRate = 5000)
    public void cleanse() {

//        try {
//            Connection connection = dataSource.getConnection();
//            Statement locationStmt = connection.createStatement();
//            LocalDate today = LocalDate.now();
//            String yesterday = today.minusDays(1).toString();
//
//            String updateLocationSQL = "DELETE FROM sanfrancisco WHERE time < '" + yesterday + "'; ";
//            locationStmt.executeUpdate(updateLocationSQL);
//            connection.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

    }

}
