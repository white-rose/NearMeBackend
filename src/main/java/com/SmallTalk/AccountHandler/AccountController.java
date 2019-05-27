package com.SmallTalk.AccountHandler;

import com.SmallTalk.LocationHandler.LocationService;
import com.SmallTalk.model.Location.LocationTag;
import com.SmallTalk.model.User.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.sql.DataSource;
import javax.websocket.server.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@RestController
@Component
public class AccountController {

  // Heroku Tables
  static final String accounts = "accounts";
  static final String sanfrancisco = "SanFrancisco";

  //  @Autowired
  //  PostgresUtil postgresUtil;
  // TODO: Do not expose API Keys
  // AWS Credentials
  private static final String accessKey = "AKIAIKMJOWW23COVBKAA";
  private static final String secretKey = "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";
  // Google API key
  private static final String apiKey = "AIzaSyDRY4sVjebmsBJsvu4fwXKTgVnOEBfIWnY";
  @Autowired LocationService locationService;
  // static BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
  @Autowired private AccountService accountService;
  @Autowired private DataSource dataSource;

  // Client for AWS DynamoDB production
  // static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withCredentials(new
  // AWSStaticCredentialsProvider(awsCreds)).withRegion("us-east-1").build();
  // static DynamoDB dynamoDB = new DynamoDB(client);
  private Logger logger = LoggerFactory.getLogger(AccountController.class);
  private Facebook facebook;
  private ConnectionRepository connectionRepository;

  // TODO: Target response times below 500ms

  @RequestMapping(
    value = "/createAccount",
    method = {RequestMethod.POST}
  )
  private void createAccount(@RequestBody User user) throws SQLException {
    Connection connection = dataSource.getConnection();
    String insertUser =
        "INSERT into users(username, firstname, lastname) "
            + "VALUES('"
            + user.getusername()
            + "','"
            + user.getFirstname()
            + "','"
            + user.getLastname()
            + "');";
    Statement createUserStatement = connection.createStatement();
    createUserStatement.executeUpdate(insertUser);
  }

  @RequestMapping(
    value = "/account",
    method = {RequestMethod.GET}
  )
  private User getAccount(@RequestParam String facebookId) throws SQLException {
    Connection connection = dataSource.getConnection();
    String getUserSql = "Select * from users where facebookid = '" + facebookId + "'";
    Statement getUserStatement = connection.createStatement();
    ResultSet resultSet = getUserStatement.executeQuery(getUserSql);
    User retrievedUser = new User();
    while (resultSet.next()) {
      retrievedUser.setusername(resultSet.getString("username"));
      retrievedUser.setFirstname(resultSet.getString("firstname"));
      retrievedUser.setLastname(resultSet.getString("lastname"));
      retrievedUser.setFacebookId(resultSet.getString("facebook_id"));
      retrievedUser.setSchool(resultSet.getString("school"));
    }
    return retrievedUser;
  }

  @RequestMapping(value = "/pullAllUsers", method = RequestMethod.GET)
  private List<User> getAllUsers() {
    Set<User> users = new HashSet<>(accountService.pullNearbyUsers());
    RankingEngine rankingEngine = new RankingEngine();
    User loggedInUser = new User();
    loggedInUser.setSchool("Stanford");
    return rankingEngine.rankByRelevance(loggedInUser, users);
  }

  @RequestMapping(value = "/pullNearbyUsers", method = RequestMethod.POST)
  private Set<User> pullNearbyUsers(@RequestParam String locality, @RequestParam int zipCode) {
    long beginningTime = System.currentTimeMillis();

    List<LocationTag> usersNearby = new ArrayList<>();
    final Set<User> nearbyUsers =
        new TreeSet<>(
            (o1, o2) -> {
              if (o1.getusername().equals(o2.getusername())) return 0;
              else return 1;
            });

    Set<User> users = new HashSet();
    List<LocationTag> previousCheckIns = new ArrayList<>();

    // Sort users by most interaction

    try (Connection connection = dataSource.getConnection()) {
      Statement createDummyData = connection.createStatement();
      locality = locality.replaceAll(" ", "");
      String selectQuery = "SELECT * from " + "sanfrancisco";
      ResultSet rs = createDummyData.executeQuery(selectQuery);
      while (rs.next()) {
        LocationTag locationTag = new LocationTag();
        if (rs.getString("zipcode") != null)
          locationTag.setzipcode(Integer.valueOf(rs.getString("zipcode")));
        locationTag.setUsername(rs.getString("username"));
        previousCheckIns.add(locationTag);
      }

    } catch (SQLException ex) {
      System.out.println(ex.getMessage());
    }

    previousCheckIns.forEach(
        locationTag -> {
          List<User> usersFound = accountService.findByUsername(locationTag.getUsername());
          if (locationTag.getzipcode() == zipCode) {
            nearbyUsers.addAll(usersFound);
          }
        });

    //        Building userBuilding = currentUser.getBuildingOccupied();
    //        if (users.size() > userBuilding.maxCapacity)
    //            System.out.println(userBuilding.name + " has exceeded maximum capacity");

    long endTime = System.currentTimeMillis();
    System.out.println("Time to pull nearby users " + (endTime - beginningTime) + " milliseconds");
    logger.info(nearbyUsers.size() + " are occupying " + locality);

    return nearbyUsers;
  }

  @RequestMapping(
          value = "/sync",
          method = {RequestMethod.POST}
  )
  public String pullDetails(@RequestBody User user) {
    String username = "";
    try (Connection connection = dataSource.getConnection()) {
      Statement createDummyData = connection.createStatement();
      String selectFBQuery =
              "SELECT username FROM users where facebookId = '" + user.getFacebookId() + "';";
      ResultSet rs = createDummyData.executeQuery(selectFBQuery);
      while (rs.next()) {
        username = rs.getString("username");
      }
      System.out.println(username);
    } catch (SQLException ex) {
      System.out.println(ex.getMessage());
    }
    return username;
  }

  @RequestMapping(
    value = "/updateOnlineStatus",
    method = {RequestMethod.POST}
  )
  private void updateOnlineStatus(@RequestBody User user) throws SQLException {

    System.out.println(!user.getOnline());

    Connection connection = dataSource.getConnection();
    String updateOnlineStatusQuery =
        "UPDATE users "
            + "SET online = '"
            + user.getOnline()
            + "' "
            + "WHERE facebookid = '"
            + user.getFacebookId()
            + "';";
    Statement onlineStatement = connection.createStatement();
    onlineStatement.executeUpdate(updateOnlineStatusQuery);
  }

  @RequestMapping(
    value = "/updateLocation",
    method = {RequestMethod.POST}
  )
  private void updateLocation(@RequestParam Double longitude,
                              @RequestParam Double latitude,
                              @RequestBody User user) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      Statement createDummyData = connection.createStatement();

      logger.info(user.toString());

//      ﻿    INSERT into sanfrancisco (username, locality, longitude, latitude)
//      VALUES('SFNATHAN', 'HOME', 12.1234, 45.4564);

      String insertQuery =
          "INSERT INTO SANFRANCISCO (username, locality, longitude, latitude, timestamp) VALUES ("
              + "'"
              + user.getusername()
              + "',"
              + "'"
              + user.getLocality()
              + "',"
              + latitude
              + ","
              + longitude
              + ",'"
              + LocalDate.now().toString()
              + "');";
      createDummyData.executeUpdate(insertQuery);

      updateOnlinePresence(user);
      updateLastLocation(user);

    } catch (SQLException ex) {
      System.out.println(ex.getMessage());
    }
  }

  // Can't update location with quotes
  private void updateLastLocation(User user) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      Statement updateOnlineStmt = connection.createStatement();
      String onlineUpdate =
          "UPDATE users set lastLocation = '"
              + user.getLocality()
              + "'where facebookId = '"
              + user.getFacebookId()
              + "'";
      updateOnlineStmt.executeUpdate(onlineUpdate);
    }
  }

  private void updateOnlinePresence(User user) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      Statement updateOnlineStmt = connection.createStatement();
      String onlineUpdate =
          "UPDATE users set online = true where facebookId = '" + user.getFacebookId() + "'";
      updateOnlineStmt.executeUpdate(onlineUpdate);
    }
  }

  @RequestMapping(
    value = "/account",
    method = {RequestMethod.PUT}
  )
  public void updateAccount(@RequestBody User user) {

    try (Connection connection = dataSource.getConnection()) {
      Statement updateUserStatement = connection.createStatement();
      String updateUserSql =
          "UPDATE users set firstName = '"
              + user.getFirstname()
              + "' where facebookid = '"
              + user.getFacebookId()
              + "'";
      updateUserStatement.executeQuery(updateUserSql);
    } catch (SQLException ex) {
      System.out.println(ex.getMessage());
    }
  }
}

    /*
        @RequestMapping(
                value = "/getFriendRequests/{user}",
                method = { RequestMethod.GET }
        )
        public List getFriendRequests(@PathVariable("user") String user) {

            GetItemRequest getItemRequest = new GetItemRequest()
                    .withTableName("Accounts")
                    .withKey(new HashMap<String, AttributeValue>() {{
                        put("username" , new AttributeValue().withS(user));
                    }});


            return ApplicationCommandLineRunner.accountsDDB.getItem(getItemRequest).getItem().get("friendRequests").getSS();

        }

        @RequestMapping(
                value = "/sendFriendRequest",
                method = { RequestMethod.POST }
        )
        public void sendFriendRequest(@RequestBody FriendRequest friendRequest) {

    //        GetItemRequest getFriendRequests = new GetItemRequest()
    //                .withTableName("Accounts")
    //                .withKey(new HashMap<String, AttributeValue>() {{
    //                    put("username" , new AttributeValue().withS("nathan"));
    //                }});
    //        GetItemResult getItemResult = ApplicationCommandLineRunner.accountsDDB.getItem(getFriendRequests);
    //        getItemResult.getItem().get("friendRequests");

            AttributeValueUpdate attributeValueUpdate = new AttributeValueUpdate();
            attributeValueUpdate.setValue(new AttributeValue()
                    .withSS(friendRequest.fromName));

            UpdateItemRequest addFriendRequest = new UpdateItemRequest()
                    .withTableName("Accounts")
                    .withKey(new HashMap<String, AttributeValue>() {{
                        put("username" , new AttributeValue().withS(friendRequest.toName));
                    }})
                    .withAttributeUpdates(new HashMap<String, AttributeValueUpdate>() {{
                        put("friendRequests" , attributeValueUpdate);
                    }});
            ApplicationCommandLineRunner.accountsDDB.updateItem(addFriendRequest);

            GetItemRequest getItemRequest = new GetItemRequest()
                    .withTableName("Accounts")
                    .withKey(new HashMap<String, AttributeValue>() {{
                        put("username" , new AttributeValue().withS(friendRequest.toName));
                    }});

            System.out.println(ApplicationCommandLineRunner.accountsDDB.getItem(getItemRequest));
        }

        @RequestMapping("/pull")
        public String pull () {

            GooglePlaceResult checkinLocation = new GooglePlaceResult();

            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject = new JSONObject();

            try {
                RestTemplate restTemplate = new RestTemplate();

                StringBuilder stringBuilder = new StringBuilder();
                String latitude, longitude, radius;

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

    //          GooglePlaceResult result = (GooglePlaceResult) jsonArray.get(0);

                System.out.println(response);
            } catch (UnirestException ex) {
                System.out.print(ex);
            }

            return jsonArray.toString();
        }

        public void getFacebookData (Model model) {
            model.addAttribute("facebookProfile", facebook.userOperations().getUserProfile());
            PagedList<Post> feed = facebook.feedOperations().getFeed();
            model.addAttribute("feed", feed);
        }

        public static byte[] hashPassword( final char[] password, final byte[] salt, final int interations, final int keyLength) {

            try {
                SecretKeyFactory skf = SecretKeyFactory.getInstance("Cabinboy23");
                PBEKeySpec spec = new PBEKeySpec(password, salt, interations, keyLength);
                SecretKey key = skf.generateSecret(spec);
                byte[] res = key.getEncoded();
                return res;

            } catch ( NoSuchAlgorithmException | InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }
        }
        */
