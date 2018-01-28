/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.java.Services;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import model.GooglePlaceResult;
import model.Greeting;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class NearMeApplication {

  private static final String template = "The device token is , %s!";
  private final AtomicLong counter = new AtomicLong();

  @Autowired
  DataSource dataSource;

  /**
   * <a href="https://cloud.google.com/appengine/docs/flexible/java/how-instances-are-managed#health_checking">
   * App Engine health checking</a> requires responding with 200 to {@code /_ah/health}.
   */
  @RequestMapping("/_ah/health")
  public String healthy() {
    // Message body required though ignored
    return "Still surviving.";
  }

  @RequestMapping("/test")
  public void test() throws SQLException {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      System.out.print("Sucks to suck");
    }
    Connection connection = dataSource.getConnection();
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("DROP TABLE IF EXISTS ticks");
    stmt.executeUpdate("CREATE TABLE ticks (tick timestamp)");
    stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
    ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");
    while (rs.next()) {
      System.out.println("Read from DB: " + rs.getTimestamp("tick"));
    }
  }

  /*
  Nathan Iphone 7 device token - 847748fb896318b4cf5ab22884c5046a002ad3206d3930ad24a8003c1b91b4a7
  */

  @RequestMapping("/requestResturant")
  //UserID as parameter?
  public Greeting notification(@RequestParam(value="token") String deviceToken) {
      ApnsService service = APNS.newService()
              .withCert("/Users/nathannguyen/Documents/Code/NearMeBackend/src/main/resources/Certificates.p12", "Cabinboy23")
              .withSandboxDestination()
              .build();

      String payload = APNS.newPayload()
              .alertBody("Simple!")
              .alertTitle("Test 123")
              .build();

    deviceToken = "847748fb896318b4cf5ab22884c5046a002ad3206d3930ad24a8003c1b91b4a7";

    service.push(deviceToken, payload);

    return new Greeting(counter.incrementAndGet(), String.format(template, deviceToken));
  }

  //API Key = AIzaSyBWdayUxe65RUQLv4QL6GcB_UXoxVlhaW0
  @RequestMapping("/pull")
  public String pull () {

    GooglePlaceResult checkinLocation = new GooglePlaceResult();

    JSONArray jsonArray = new JSONArray();
    JSONObject jsonObject = new JSONObject();

    try {
      RestTemplate restTemplate = new RestTemplate();

      StringBuilder stringBuilder = new StringBuilder();
      String latitude, longitude, radius;
      String APIkey = "AIzaSyBWdayUxe65RUQLv4QL6GcB_UXoxVlhaW0";

      HttpResponse<String> response = Unirest.get("https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
              "location=37.779758, -122.404139" +
              "&radius=31" +
              "&key=AIzaSyBWdayUxe65RUQLv4QL6GcB_UXoxVlhaW0")
              .header("cache-control", "no-cache")
              .header("postman-token", "187d7feb-72b8-3e90-5874-44255f0b1dd5")
              .asString();

      jsonObject = new JSONObject(response.getBody());
      jsonArray = (JSONArray) jsonObject.get("results");
      String firstPlace = jsonArray.get(0).toString();
      System.out.println(firstPlace);
      Gson gson = new Gson();
      checkinLocation = gson.fromJson(firstPlace, GooglePlaceResult.class);

//    GooglePlaceResult result = (GooglePlaceResult) jsonArray.get(0);

      System.out.println(response);
    } catch (UnirestException ex) {
      System.out.print(ex);
    }

    return jsonArray.toString();
  }


}
