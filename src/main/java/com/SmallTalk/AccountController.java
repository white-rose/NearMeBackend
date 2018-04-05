package com.SmallTalk;

import com.SmallTalk.model.FriendRequest;
import com.SmallTalk.model.GooglePlaceResult;
import com.SmallTalk.model.UserAccount;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.sql.DataSource;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Component
public class AccountController {

    @Autowired
    DataSource dataSource;

    private Logger logger = LoggerFactory.getLogger(AccountController.class);

    //TODO: Do not expose API Keys
    //AWS Credentials
    private static final String accessKey = "AKIAIKMJOWW23COVBKAA";
    private static final String secretKey = "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";
    static BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);

    //Google API key
    private final static String apiKey = "AIzaSyDRY4sVjebmsBJsvu4fwXKTgVnOEBfIWnY";

    // Client for AWS DynamoDB production
    // static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion("us-east-1").build();
    // static DynamoDB dynamoDB = new DynamoDB(client);

    private Facebook facebook;

    //Heroku Tables
    static final String accounts = "accounts";
    static final String sanfrancisco = "sanfrancisco";

    private ConnectionRepository connectionRepository;

    @RequestMapping(
            value = "/pullAccounts",
            method = RequestMethod.POST)
    private List<UserAccount> pullAccounts (@RequestBody UserAccount currentAccount) throws SQLException {

        List<UserAccount> userAccounts = new ArrayList<>();

        long beginningTime = System.currentTimeMillis();
        Statement stmt = dataSource.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT DISTINCT accounts.firstname, accounts.lastname, accounts.facebookid " +
                "FROM accounts " +
                "inner join sanfrancisco " +
                "on accounts.facebookid=sanfrancisco.facebookid " +
                "WHERE locality = '" + currentAccount.getLocality() +
                "' AND time <= '" + LocalDate.now() + "' " +
                "AND ONLINE = true");

        while (rs.next()) {
            UserAccount userAccount = new UserAccount();
            userAccount.setFacebookId(rs.getString("facebookid"));
            userAccount.setFirstName(rs.getString("firstname"));
            userAccount.setLastName(rs.getString("lastname"));
            userAccounts.add(userAccount);
        }

        long endTime = System.currentTimeMillis();
        System.out.println((endTime - beginningTime));

        return userAccounts;

    }

    @RequestMapping(
            value = "/updateOnlineStatus",
            method = { RequestMethod.PUT })
    private void updateOnlineStatus (@RequestBody com.SmallTalk.model.UserAccount userAccount) throws SQLException {

        System.out.println(!userAccount.getOnline());

        Connection connection = dataSource.getConnection();
        String updateOnlineStatusQuery = "UPDATE accounts " +
                "SET online = '" + userAccount.getOnline() + "' " +
                "WHERE facebookid = '" + userAccount.getFacebookId() + "';";
        Statement onlineStatement = connection.createStatement();
        onlineStatement.executeUpdate(updateOnlineStatusQuery);
    }

    @RequestMapping(
            value = "/createAccount/firstname/{firstname}/lastname/{lastname}/password/{password}",
            method = { RequestMethod.POST })
    private void createAccount (@PathVariable String firstname, @PathVariable String lastname, @PathVariable String password) throws SQLException {

        Connection connection = dataSource.getConnection();
        String createAccountQuery = "insert into " + accounts + "(username, lastname, online, facebookid, lastlocation, firstname)"
                + "VALUES (" ;


    }

    private void checkin () {}

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

    private void AWSDyamoDBQueries () {

//      System.setProperty("sqlite4java.library.path", "/Users/nathannguyen/Documents/Code/sqlite4java");
//        UserAccount[] newUser = new UserAccount[1];
//        ScanResult allResults = ApplicationCommandLineRunner.accountsDDB.scan("Accounts"
//                , Arrays.asList("username","FirstName","Locality","friendRequests","sex","friends","facebookId"));
//        allResults.getItems().stream().forEach(item -> {
//            UserAccount userAccount = new UserAccount();
//            userAccount.setFirstName(item.get("FirstName").getS());
//            userAccount.setLocality(item.get("Locality").getS());
//            userAccount.setUserName(item.get("username").getS());
////            userAccount.setFriendRequests(item.get("friendRequests").getSS());
//            if (item.get("facebookId") != null) {
//                userAccount.setFacebookId(item.get("facebookId").getS());
//            }
//            userAccount.setFriends(item.get("friends").getSS());
//            userAccount.setSex("MALE");
//            userAccounts.add(userAccount);
//        });

        //        Table table = dynamoDB.getTable("accounts");
//        try {
//            Item item = new Item().withPrimaryKey("username", firstname)
//                    .withString("firstName", firstname)
//                    .withString("lastName", lastname)
//                    .withString("password", password);
//            table.putItem(item);
//        } catch (Exception e) {
//            System.err.println("Create items failed.");
//            System.err.println(e.getMessage());
//        }

    }

}
