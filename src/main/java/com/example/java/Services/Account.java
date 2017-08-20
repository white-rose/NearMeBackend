package com.example.java.Services;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.Post;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

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

    public Account () {

    }



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
