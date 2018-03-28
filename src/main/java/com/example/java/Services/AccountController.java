package com.example.java.Services;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import model.DetailedResponse;
import model.FriendRequest;
import model.GooglePlaceResult;
import model.UserAccount;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@Component
public class AccountController {

    String username;
    String firstName;
    String lastName;
    String locality;

    @Autowired
    DataSource dataSource;

    private static final String accessKey = "AKIAIKMJOWW23COVBKAA";
    private static final String secretKey = "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";
    private static final String template = "The device token is , %s!";
    private final AtomicLong counter = new AtomicLong();
    static BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
    static final String tableName = "accounts";
    private Facebook facebook;
    private ConnectionRepository connectionRepository;
    private Logger logger = LoggerFactory.getLogger(AccountController.class);

    private final static String apiKey = "AIzaSyDRY4sVjebmsBJsvu4fwXKTgVnOEBfIWnY";

    // Client for AWS DynamoDB production
    // static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion("us-east-1").build();
    // static DynamoDB dynamoDB = new DynamoDB(client);

    @RequestMapping(
            value = "/pullAccounts",
            method = RequestMethod.POST)
    public List<UserAccount> pullAccounts (@RequestBody UserAccount currentAccounts) throws SQLException {

        List<UserAccount> userAccounts = new ArrayList<>();

        long beginningTime = System.currentTimeMillis();
        Statement stmt = dataSource.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT DISTINCT accounts.firstname, accounts.facebookid " +
                "FROM accounts " +
                "inner join sanfrancisco " +
                "on accounts.facebookid=sanfrancisco.facebookid " +
                "WHERE locality = '" + currentAccounts.getLocality() +
                "' AND time < '" + LocalDate.now() + "'" +
                "AND ONLINE = true");

        while (rs.next()) {
            UserAccount userAccount = new UserAccount();
            userAccount.setFacebookId(rs.getString("facebookid"));
            userAccount.setFirstName(rs.getString("firstname"));
            userAccounts.add(userAccount);
        }

        //Print response time
        long endTime = System.currentTimeMillis();
        System.out.println((endTime - beginningTime));

        return userAccounts;

        //AWS Dynamo DB

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
    }

    @RequestMapping(value = "/createAccount/firstname/{firstname}/lastname/{lastname}/password/{password}")
    public void createAccount (@PathVariable String firstname, @PathVariable String lastname, @PathVariable String password) {

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

    @RequestMapping("/checkNearby/latitude/{latitude}/longitude/{longitude}")
    public String checkIn(@PathVariable String latitude, @PathVariable String longitude) throws JsonProcessingException {

        final StringBuilder locationUrl = new StringBuilder();
        locationUrl.append("https://maps.googleapis.com/maps/api/place/nearbysearch/json?")
                   .append(String.valueOf(latitude))
                   .append(",")
                   .append(longitude)
                   .append("&radius=1000&")
                   .append("key" + this.apiKey);

        Logger logger = LoggerFactory.getLogger(AccountController.class);
        logger.info(locationUrl.toString());

        RestTemplate restTemplate = new RestTemplate();
        DetailedResponse result = restTemplate.getForObject(locationUrl.toString(), DetailedResponse.class);
        GooglePlaceResult[] places = result.getPlaces();
        System.out.print(places[0].getIcon());

        ObjectMapper mapper = new ObjectMapper();

        String jsonPlaceString = mapper.writeValueAsString(places[0]);
        logger.info("Results came back as " + jsonPlaceString);

        return jsonPlaceString;
    }

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

    //Send Push Notication to phone
//    @RequestMapping("/samplePush")
//    public Greeting notification(@RequestParam(value="token") String deviceToken) {
//        ApnsService service = APNS.newService()
//                .withCert("/Users/nathannguyen/Documents/Code/NearMeBackend/src/main/resources/Certificates.p12", "Cabinboy23")
//                .withSandboxDestination()
//                .build();
//
//        String payload = APNS.newPayload()
//                .alertBody("Simple!")
//                .alertTitle("Test 123")
//                .build();
//
//        deviceToken = "c8ad4e8b7a96943039b3ea89a6a5508bc6426953fdbadfeae06c970b28a495c0";
//
//        service.push(deviceToken, payload);
//
//        return new Greeting(counter.incrementAndGet(), String.format(template, deviceToken));
//    }

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

}
