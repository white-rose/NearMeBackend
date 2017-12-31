package com.example.java.Services;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.DetailedResponse;
import model.GooglePlaceResult;
import model.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Map;

@RestController
public class Account {

    String username;
    String firstName;
    String lastName;

    static BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAIKMJOWW23COVBKAA", "pUlGQxF4y9Hwvs28nqEgrXk7kcoRnFw29aacFRjA");

    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds)).withRegion("us-east-1").build();
    static DynamoDB dynamoDB = new DynamoDB(client);

    static String tableName = "accounts";

    private Facebook facebook;
    private ConnectionRepository connectionRepository;

    private Logger logger = LoggerFactory.getLogger(Account.class);

    private final static String apiKey = "AIzaSyDRY4sVjebmsBJsvu4fwXKTgVnOEBfIWnY";

    public Account () {}

    @RequestMapping(value = "/createAccount/firstname/{firstname}/lastname/{lastname}/password/{password}")
    public void createAccount (@PathVariable String firstname, @PathVariable String lastname, @PathVariable String password) {

        Table table = dynamoDB.getTable("accounts");
        try {
            Item item = new Item().withPrimaryKey("username", firstname)
                    .withString("firstName", firstname)
                    .withString("lastName", lastname)
                    .withString("password", password);
            table.putItem(item);
        } catch (Exception e) {
            System.err.println("Create items failed.");
            System.err.println(e.getMessage());
        }
    }

    @RequestMapping("/login")
    public void login() {
        System.out.print("User logging in");
        //Login to Account
    }

    @RequestMapping("/retrieveAccounts")
    public String retrieveAccounts () {

        ScanRequest scanRequest = new ScanRequest().withTableName("accounts");

        ScanResult result = client.scan(scanRequest);
        for (Map<String, AttributeValue> item : result.getItems()) {
           if (item.containsKey("sex")) {
               System.out.println(item);
           }
        }

        return result.toString();
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

        Logger logger = LoggerFactory.getLogger(Account.class);
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

    //Local DynamoDB
    @RequestMapping("/pullAccountsLocal")
    public UserAccount pullAccounts () {
        System.setProperty("sqlite4java.library.path", "/Users/nathannguyen/Documents/Code/sqlite4java");

//      AmazonDynamoDB ddb = DynamoDBEmbedded.create().amazonDynamoDB();
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-west-2").build();

        DescribeTableResult describeTableResult = client.describeTable("Accounts");
        System.out.println("Describing table is: " + describeTableResult.toString());

        final UserAccount[] newUser = new UserAccount[1];
        ScanResult allResults = client.scan("Accounts", Arrays.asList("username","FirstName","Locality"));
        allResults.getItems().forEach(item -> {
            newUser[0] = new UserAccount();
            newUser[0].setFirstName(item.get("username").getS());
            newUser[0].setLastName(item.get("FirstName").getS());
            newUser[0].setUserName(item.get("Locality").getS());
        });

        return newUser[0];

    }

    public static String getParamsString(Map<String, String> params)
                throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
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
