package com.example.java.Services;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.DetailedResponse;
import model.FriendRequest;
import model.GooglePlaceResult;
import model.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class AccountController {

    String username;
    String firstName;
    String lastName;
    String locality;

    @Autowired
    DataSource dataSource;

    private static final String accessKey = "AKIAIKMJOWW23COVBKAA";
    private static final String secretKey = "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";

    static BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);

    static final String tableName = "accounts";

    private Facebook facebook;
    private ConnectionRepository connectionRepository;

    private Logger logger = LoggerFactory.getLogger(AccountController.class);

    private final static String apiKey = "AIzaSyDRY4sVjebmsBJsvu4fwXKTgVnOEBfIWnY";

    //Client for AWS DynamoDB production
    //static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion("us-east-1").build();
//    static DynamoDB dynamoDB = new DynamoDB(client);

    @RequestMapping(
            value = "/updateLocation",
            method = { RequestMethod.POST }
    )
    public void updateLocation (@RequestBody model.Account userAccount) throws SQLException {

//          ApplicationCommandLineRunner.accountsDDB.putItem(new PutItemRequest()
//          .withTableName("Accounts")
//          .withItem(new HashMap() {{
//              put("FirstName", new AttributeValue().withS(userAccount.getFirstName()));
//              put("Locality", new AttributeValue().withS(userAccount.getLocality()));
//              put("username", new AttributeValue().withS(userAccount.getUsername()));
//              put("facebookid", new AttributeValue().withS(userAccount.getFacebookId()));
//              put("friends", new AttributeValue().withSS("Nathan", "Billy"));
//              put("friendRequests", new AttributeValue().withSS("none"));
//          }}));

        Connection connection = dataSource.getConnection();
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("INSERT INTO accounts (username, firstname, facebookId) " +
                "VALUES ('" + userAccount.getUsername() + "','" + userAccount.getFirstName() + "', '" + userAccount.getFacebookId() +"')");

    }

    @RequestMapping("/pullAccounts")
    public List<UserAccount> pullAccounts () throws SQLException{
//        System.setProperty("sqlite4java.library.path", "/Users/nathannguyen/Documents/Code/sqlite4java");
//
        List<UserAccount> userAccounts = new ArrayList<>();
//
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

        Statement stmt = dataSource.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM accounts");
        while (rs.next()) {
            UserAccount userAccount = new UserAccount();
            userAccount.setUserName(rs.getString("username"));
            userAccount.setFirstName(rs.getString("firstName"));
            userAccount.setFacebookId(rs.getString("facebookid"));
            userAccounts.add(userAccount);
        }

        return userAccounts;
//                .stream()
//                .filter(males -> !"FEMALE".equals(males.getSex()))
//                .collect(Collectors.toList());

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
