package com.SmallTalk.LocationHandler;

import com.SmallTalk.model.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class LocationController implements LocationControllerDefinition {

  @Autowired private LocationService locationService;

  @RequestMapping(
    value = "/track",
    method = RequestMethod.POST,
    consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE}
  )
  public void trackLocation(
      @RequestParam Double longitude,
      @RequestParam Double latitude,
      @RequestParam int zipCode,
      @RequestBody User user) {

    locationService.trackLocation(user, longitude, latitude, zipCode);
  }

  // @Override
  //    @Scheduled(fixedRate = 5000)
  // public void cleanse() {

  //        try {
  //            Connection connection = dataSource.getConnection();
  //            Statement locationStmt = connection.createStatement();
  //            LocalDate today = LocalDate.now();
  //            String yesterday = today.minusDays(1).toString();
  //
  //            String updateLocationSQL = "DELETE FROM sanfrancisco WHERE time < '" + yesterday +
  // "'; ";
  //            locationStmt.executeUpdate(updateLocationSQL);
  //            connection.close();
  //        } catch (SQLException e) {
  //            e.printStackTrace();
  //        }

  // }

}
