package com.SmallTalk.AccountHandler;

import com.SmallTalk.LocationHandler.LocationService;
import com.SmallTalk.model.Location.LocationTag;
import com.SmallTalk.model.User.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@RestController
@Component
public class AccountController {

    @Autowired
    AccountService accountService;

    @Autowired
    LocationService locationService;

//    @Autowired
//    PostgresUtil postgresUtil;

    @Autowired
    DataSource dataSource;

    private Logger logger = LoggerFactory.getLogger(AccountController.class);

    //TODO: Do not expose API Keys
    //AWS Credentials
    private static final String accessKey = "AKIAIKMJOWW23COVBKAA";
    private static final String secretKey = "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";
//    static BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);

    //Google API key
    private final static String apiKey = "AIzaSyDRY4sVjebmsBJsvu4fwXKTgVnOEBfIWnY";

    private Facebook facebook;

    // Client for AWS DynamoDB production
    // static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion("us-east-1").build();
    // static DynamoDB dynamoDB = new DynamoDB(client);

    //Heroku Tables
    static final String accounts = "accounts";
    static final String sanfrancisco = "SanFrancisco";

    private ConnectionRepository connectionRepository;

    //TODO: Target response times below 500ms

    @RequestMapping(
            value = "/createAccount",
            method = {RequestMethod.POST})
    private void createAccount(@RequestBody User user) throws SQLException {
        Connection connection = dataSource.getConnection();
        String insertUser = "INSERT into users(username, firstname, lastname) " +
                "VALUES('" + user.getusername() + "','" +
                user.getFirstname() + "','" +
                user.getLastname() + "');";
        Statement createUserStatement = connection.createStatement();
        createUserStatement.executeUpdate(insertUser);
    }

    @RequestMapping(
            value = "/pullNearbyUsers",
            method = RequestMethod.POST)
    private Set<User> pullNearbyUsers(@RequestParam String locality) {

        long beginningTime = System.currentTimeMillis();

        List<LocationTag> usersNearby = new ArrayList<>();
        final Set<User> nearbyUsers = new TreeSet<>((o1, o2) -> {
            if (o1.getusername().equals(o2.getusername()))
                return 0;
            else
                return 1;
        });
        
        if (locality.equals(sanfrancisco))
            usersNearby = locationService.pullNearbyUsers();

        usersNearby.forEach(tag -> {
                    nearbyUsers.addAll(accountService.findByUsername(tag.getUsername()));
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
            value = "/updateOnlineStatus",
            method = {RequestMethod.POST})
    private void updateOnlineStatus(@RequestBody User user) throws SQLException {

        System.out.println(!user.getOnline());

        Connection connection = dataSource.getConnection();
        String updateOnlineStatusQuery = "UPDATE users " +
                "SET online = '" + user.getOnline() + "' " +
                "WHERE facebookid = '" + user.getFacebookId() + "';";
        Statement onlineStatement = connection.createStatement();
        onlineStatement.executeUpdate(updateOnlineStatusQuery);
    }

    @RequestMapping(
            value = "/updateLocation",
            method = {RequestMethod.POST})
    private void updateLocation(@RequestBody User user) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            Statement createDummyData = connection.createStatement();
            //String deleteQuery = "delete from sanfrancisco where facebookid='" + i + "';";
            String insertQuery = "INSERT INTO SANFRANCISCO (username, locality, time) VALUES ("
                    + "'" + user.getusername() + "',"
                    + "'" + user.getLocality() + "',"
                    + "'" + LocalDate.now().toString() + "');";
            createDummyData.executeUpdate(insertQuery);

            updateOnlinePresence(user);
            updateLastLocation(user);

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    //Can't update location with quotes 
    private void updateLastLocation(User user) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            Statement updateOnlineStmt = connection.createStatement();
            String onlineUpdate = "UPDATE users set lastLocation = '"
                    + user.getLocality()
                    + "'where facebookId = '" + user.getFacebookId() + "'";
            updateOnlineStmt.executeUpdate(onlineUpdate);
        }
    }

    private void updateOnlinePresence(User user) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            Statement updateOnlineStmt = connection.createStatement();
            String onlineUpdate = "UPDATE users set online = true where facebookId = '" + user.getFacebookId() + "'";
            updateOnlineStmt.executeUpdate(onlineUpdate);
        }
    }

    @RequestMapping(
            value = "/sync",
            method = {RequestMethod.POST})
    public String pullDetails(@RequestBody User user) {
        String username = "";
        try (Connection connection = dataSource.getConnection()) {
            Statement createDummyData = connection.createStatement();
            String SelectFBQuery = "SELECT username FROM users where facebookId = '" + user.getFacebookId() + "';";
            ResultSet rs = createDummyData.executeQuery(SelectFBQuery);
            while (rs.next()) {
                username = rs.getString("username");
            }
            System.out.println(username);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return username;
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

