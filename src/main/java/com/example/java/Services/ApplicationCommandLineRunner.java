package com.example.java.Services;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import model.LocationTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Component
public class ApplicationCommandLineRunner implements CommandLineRunner {

    public static AmazonDynamoDB accountsDDB;

    @Autowired
    DataSource dataSource;

    @Override
    public void run(String... strings) throws Exception {
//        dummyAccountData();
          dummyHistoryData();
    }

//    @Bean
//    public CommandLineRunner demo(AccountRepository repository) {
//        return (args -> {
//            Account myAccount = new Account();
//            myAccount.setFirstName("Buddy");
//            myAccount.setFacebookId("123456789");
//            repository.save(myAccount);
//
//            System.out.println("Customers found with foundAll():");
//            System.out.println("-------------------------------");
//            for (Account account : repository.findAll()) {
//                System.out.println(account.getFacebookId());
//            }
//        });
//    }

    private void dummyAccountData() {

        for (int i = 0; i < 100; i++) {
            try (
                    Connection connection = dataSource.getConnection()) {
                Statement createDummyData = connection.createStatement();
                String insertQuery = "insert into accounts (username, lastname, firstname, facebookid, online) " +
                        "VALUES ('" + randomIdentifier() + "', '"+  randomIdentifier() + "','" + randomIdentifier() + "', " + i + " , true);";
                String deleteQuery = "delete from accounts where facebookid = '" + i + "'";
                createDummyData.executeUpdate(deleteQuery);

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

    private void dummyHistoryData() {

        final String BRANNAN_APARTMENTS = "855 Brannan Apartments";
        final String UNIVERSITY_OF_SANFRACISCO = "University of San Francisco";

        for (int i = 0; i < 100; i++) {
            try (
                    Connection connection = dataSource.getConnection()) {
                    Statement createDummyData = connection.createStatement();
                    String deleteQuery = "delete from sanfrancisco where facebookid='" + i + "';";
                    String insertQuery = "INSERT INTO sanfrancisco (facebookid, locality, time) VALUES ("
                            + "'" + i + "',"
                            + "'" + UNIVERSITY_OF_SANFRACISCO + "',"
                            + "'" + LocalDate.now().toString() + "');";
                    createDummyData.executeUpdate(insertQuery);

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

    public void insertLocationTag(LocationTag locationTag) {

        Random rand = new Random();

        String SQL = "INSERT INTO sanfrancisco (facebookid, locality, time)"
                + "VALUES (?, ?, ?)";

        long id = 0;


        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
             pstmt.setString(1, locationTag.getFacebookId());
             pstmt.setString(2, locationTag.getLocality());
             pstmt.setString(3, locationTag.getTimestamp().toString());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private String randomSex() {
        Sex[] sexes = {Sex.FEMALE, Sex.MALE};
        Random rand = new Random();
        return sexes[rand.nextInt(sexes.length)].toString();
    }

    // Returns random String
    private String randomIdentifier() {
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

    private void MondoDBPlayground () {
    //Mongo DB Testing
        /*
        MongoClientURI uri = new MongoClientURI(
            "mongodb+srv://memongodb:Mr.Robot23@cluster0-d9ogz.mongodb.net/test"
        );

        MongoClient mongoClient = new MongoClient(uri);
        ListDatabasesIterable<Document> results = mongoClient.listDatabases();
        results.forEach((Block<? super Document>) x -> System.out.print(x));

        MongoDatabase database = mongoClient.getDatabase();

        MongoClientURI uri = new MongoClientURI(
          "mongodb+srv://kay:testerpassword@cluster0.mongodb.net/");

        MongoClient mongoClient = new MongoClient(uri);

        MongoDatabase database = mongoClient.getDatabase("test");
        */
    }

    private void AWSDynamoDBPlayground() {
    /*
      System.setProperty("sqlite4java.library.path", "/Users/nathannguyen/Documents/Code/sqlite4java");

//      accountsDDB = DynamoDBEmbedded.create().amazonDynamoDB();
//      californiaDDB = DynamoDBEmbedded.create().amazonDynamoDB();
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

//      accountsDDB.createTable(createTableRequest);
//      californiaDDB.createTable(createTableRequest1);

      HashMap BillyAttributes = new HashMap() {{
            put("FirstName", new AttributeValue().withS("Billy"));
            put("Username", new AttributeValue().withS("tester"));
            put("Password", new AttributeValue().withS("Test"));
      }};

      PutRequest Billy = new PutRequest()
              .withItem(BillyAttributes);

      ArrayList allWriteRequests = new ArrayList() {{
          add(new WriteRequest().withPutRequest(James));
          add(new WriteRequest().withPutRequest(Sam));
          add(new WriteRequest().withPutRequest(Sally));
          add(new WriteRequest().withPutRequest(Bob));
          add(new WriteRequest().withPutRequest(Billy));
          add(new WriteRequest().withPutRequest(Greg));
          add(new WriteRequest().withPutRequest(Michael));
          add(new WriteRequest().withPutRequest(Elise));
      }};

      allWriteRequests.add(new WriteRequest());
      HashMap moreItems = new HashMap() {{
          put("String", allWriteRequests);
      }};
      BatchWriteItemRequest populateItems = new BatchWriteItemRequest()
              .withRequestItems(moreItems);
        client.batchWriteItem(populateItems);

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


      californiaDDB.putItem(new PutItemRequest()
            .withTableName("California")
            .withItem(new HashMap() {{
                put("")
            }}));
            */
    }

    private void multipleLocationUpdateRequests () {
    /*
    SanFranciscoTag sanFranciscoTag = new SanFranciscoTag(
            "1",
            "University of San Francisco",
            now()
    );
    insertLocationTag(sanFranciscoTag);

    ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
    PoolingNHttpClientConnectionManager cm =
            new PoolingNHttpClientConnectionManager(ioReactor);
    CloseableHttpAsyncClient client =
            HttpAsyncClients.custom().setConnectionManager(cm).build();
    client.start();


    Future<HttpResponse<JsonNode>> future = Unirest.post("https://fathomless-gorge-73815.herokuapp.com/updateLocation")
            .header("accept", "application/json")
            .asJsonAsync(new Callback<JsonNode>() {
                @Override
                public void completed(HttpResponse<JsonNode> httpResponse) {

                }

                @Override
                public void failed(UnirestException e) {

                }

                @Override
                public void cancelled() {
                }
            });
    */
    }


}

enum Sex {
    MALE, FEMALE;
}
