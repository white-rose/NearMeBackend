package com.example.java.Services;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.model.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

@Component
public class ApplicationCommandLineRunner implements CommandLineRunner {

    public static AmazonDynamoDB accountsDDB;
    public static AmazonDynamoDB californiaDDB;

    @Override
    public void run(String... strings) throws Exception {

      System.setProperty("sqlite4java.library.path", "/Users/nathannguyen/Documents/Code/sqlite4java");

      accountsDDB = DynamoDBEmbedded.create().amazonDynamoDB();
      californiaDDB = DynamoDBEmbedded.create().amazonDynamoDB();
//      accountsDDB = null;
//      californiaDDB = null;
    //    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-west-2").build();

      String usernameAttribute = "username";

      ProvisionedThroughput provisionedthroughput = new ProvisionedThroughput(1000L, 1000L);

      List<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>() {{
          add(new KeySchemaElement()
                  .withAttributeName(usernameAttribute)
                  .withKeyType(KeyType.HASH));
      }};

      List<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>() {
      {
          add(new AttributeDefinition()
                  .withAttributeName(usernameAttribute)
                  .withAttributeType("S"));
      }};

      CreateTableRequest createTableRequest = new CreateTableRequest()
              .withTableName("Accounts")
              .withKeySchema(keySchema)
              .withAttributeDefinitions(attributeDefinitions)
              .withProvisionedThroughput(new ProvisionedThroughput()
                      .withReadCapacityUnits(5L)
                .withWriteCapacityUnits(6L));

        List<KeySchemaElement> keySchema1 = new ArrayList<KeySchemaElement>() {{
            add(new KeySchemaElement()
                    .withAttributeName(usernameAttribute)
                    .withKeyType(KeyType.HASH));
        }};

        List<AttributeDefinition> attributeDefinitions1 = new ArrayList<AttributeDefinition>() {
            {
                add(new AttributeDefinition()
                        .withAttributeName(usernameAttribute)
                        .withAttributeType("S"));
            }};

        CreateTableRequest createTableRequest1 = new CreateTableRequest()
                .withTableName("California")
                .withKeySchema(keySchema1)
                .withAttributeDefinitions(attributeDefinitions1)
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits(5L)
                        .withWriteCapacityUnits(6L));

      accountsDDB.createTable(createTableRequest);
      californiaDDB.createTable(createTableRequest1);

      HashMap BillyAttributes = new HashMap() {{
            put("FirstName", new AttributeValue().withS("Billy"));
            put("Username", new AttributeValue().withS("tester"));
            put("Password", new AttributeValue().withS("Test"));
      }};
      HashMap JamesAttribute = new HashMap() {{
          put("FirstName", new AttributeValue().withS("James"));
          put("Username", new AttributeValue().withS("tester2"));
          put("Password", new AttributeValue().withS("Test"));
      }};
      HashMap SamAttributes = new HashMap() {{
          put("FirstName", new AttributeValue().withS("Sam"));
          put("Username", new AttributeValue().withS("tester3"));
          put("Password", new AttributeValue().withS("Test"));
      }};
      HashMap SallyAttributes = new HashMap() {{
          put("FirstName", new AttributeValue().withS("Sally"));
          put("Username", new AttributeValue().withS("tester4"));
          put("Password", new AttributeValue().withS("Test"));
      }};
      HashMap BobAttributes = new HashMap() {{
          put("FirstName", new AttributeValue().withS("Bob"));
          put("Username", new AttributeValue().withS("tester5"));
          put("Password", new AttributeValue().withS("Test"));
      }};
      HashMap GregAttributes = new HashMap() {{
          put("FirstName", new AttributeValue().withS("Greg"));
          put("Username", new AttributeValue().withS("tester5"));
          put("Password", new AttributeValue().withS("Test"));
      }};
      HashMap MichaelAttributes = new HashMap() {{
          put("FirstName", new AttributeValue().withS("Michael"));
          put("Username", new AttributeValue().withS("tester5"));
          put("Password", new AttributeValue().withS("Test"));
      }};
      HashMap EliseAttributes = new HashMap() {{
          put("FirstName", new AttributeValue().withS("Elise"));
          put("Username", new AttributeValue().withS("tester5"));
          put("Password", new AttributeValue().withS("Test"));
      }};

      PutRequest Billy = new PutRequest()
              .withItem(BillyAttributes);

//      ArrayList allWriteRequests = new ArrayList() {{
//          add(new WriteRequest().withPutRequest(James));
//          add(new WriteRequest().withPutRequest(Sam));
//          add(new WriteRequest().withPutRequest(Sally));
//          add(new WriteRequest().withPutRequest(Bob));
//          add(new WriteRequest().withPutRequest(Billy));
//          add(new WriteRequest().withPutRequest(Greg));
//          add(new WriteRequest().withPutRequest(Michael));
//          add(new WriteRequest().withPutRequest(Elise));
//      }};

//      allWriteRequests.add(new WriteRequest());
//      HashMap moreItems = new HashMap() {{
//          put("String", allWriteRequests);
//      }};
//      BatchWriteItemRequest populateItems = new BatchWriteItemRequest()
//              .withRequestItems(moreItems);
//        client.batchWriteItem(populateItems);

      for (int i = 0; i < 100; i++) {
          int finalI = i;
          accountsDDB.putItem(new PutItemRequest()
                  .withTableName("Accounts")
                  .withItem(new HashMap() {{
                      put("FirstName", new AttributeValue().withS(randomIdentifier()));
                      put("Locality", new AttributeValue().withS("Nike"));
                      put("username", new AttributeValue().withS("tester" + finalI));
                      put("facebookId", new AttributeValue().withS("12345678"));
                      put("friends", new AttributeValue().withSS("Random1", "Random2"));
                      put("friendRequests", new AttributeValue().withSS("none"));
                      put("sex", new AttributeValue().withS(String.valueOf(Sex.MALE)));
                  }}));
      }

      accountsDDB.putItem(new PutItemRequest()
              .withTableName("Accounts")
              .withItem(new HashMap() {{
                  put("FirstName", new AttributeValue().withS("Henry"));
                  put("Locality", new AttributeValue().withS("Nike"));
                  put("username", new AttributeValue().withS("Bob"));
                  put("friends", new AttributeValue().withSS("Nathan", "Bob"));
                  put("friendRequests", new AttributeValue().withSS("none"));
                  put("sex", new AttributeValue().withS(String.valueOf(Sex.MALE)));
              }}));

      accountsDDB.putItem(new PutItemRequest()
              .withTableName("Accounts")
              .withItem(new HashMap() {{
                  put("FirstName", new AttributeValue().withS("Nathan"));
                  put("Locality", new AttributeValue().withS("Nike"));
                  put("username", new AttributeValue().withS("Nathan"));
                  put("friends", new AttributeValue().withSS("Nathan", "Bob"));
                  put("friendRequests", new AttributeValue().withSS("none"));
                  put("sex", new AttributeValue().withS(String.valueOf(Sex.MALE)));
              }}));

//      californiaDDB.putItem(new PutItemRequest()
//            .withTableName("California")
//            .withItem(new HashMap() {{
//                put("")
//            }}));

    }

    private static Connection getConnection() throws URISyntaxException, SQLException {
        Properties props = new Properties();
        props.setProperty("user","yfdcultzhzhsos");
        props.setProperty("password","08357ce1b8f56cb12bfd807e64a6bdf4d7f828bba48df33ec225518595f7fa85");
        props.setProperty("ssl","true");
        String dbUrl = "postgres://ec2-54-225-230-142.compute-1.amazonaws.com:5432/dba8br45ci0k1e";
        return DriverManager.getConnection(dbUrl, props);
    }

//    @PostConstruct
//    public void myRealMainMethod() throws SQLException {
//        Statement stmt = dataSource.getConnection().createStatement();
//        stmt.executeUpdate("DROP TABLE IF EXISTS ticks");
//        stmt.executeUpdate("CREATE TABLE ticks (tick timestamp)");
//        stmt.executeUpdate("INSERT INTO ticks VALUES (now())");
//        ResultSet rs = stmt.executeQuery("SELECT tick FROM ticks");
//        while (rs.next()) {
//            System.out.println("Read from DB: " + rs.getTimestamp("tick"));
//        }
//    }

    public String randomSex() {
        Sex[] sexes = {Sex.FEMALE, Sex.MALE};
        Random rand = new Random();
        return sexes[rand.nextInt(sexes.length)].toString();
    }

    // Returns random String
    public String randomIdentifier() {
        // class variable
        final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        final java.util.Random rand = new java.util.Random();

    // consider using a Map<String,Boolean> to say whether the identifier is being used or not
        final Set<String> identifiers = new HashSet<>();

        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            int length = rand.nextInt(5)+5;
            for(int i = 0; i < length; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            if(identifiers.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }
        return builder.toString();
    }

    public static void listTables(ListTablesResult result, String method) {
        System.out.println("found " + Integer.toString(result.getTableNames().size()) + " tables with " + method);
        for(String table : result.getTableNames()) {
            System.out.println(table);
        }
    }

}

enum Sex {
    MALE, FEMALE;
}
