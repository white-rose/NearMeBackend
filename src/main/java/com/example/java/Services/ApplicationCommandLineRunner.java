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

//        ScheduledExecutorService scheduler =
//                newSingleThreadScheduledExecutor();
//
//        System.out.println(DateFormat.getDateInstance());

//        MongoClientURI uri = new MongoClientURI(
//            "mongodb+srv://memongodb:Mr.Robot23@cluster0-d9ogz.mongodb.net/test"
//        );
//
//        MongoClient mongoClient = new MongoClient(uri);
//        ListDatabasesIterable<Document> results = mongoClient.listDatabases();
//        results.forEach((Block<? super Document>) x -> System.out.print(x));/**/

//        MongoDatabase database = mongoClient.getDatabase()

//        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
//        Runnable task = () -> {
//            try {
//                Connection connection = dataSource.getConnection();
//                Statement locationStmt = connection.createStatement();
//                String updateLocationSQL = "DELETE FROM sanfrancisco WHERE time < " + "\'2018-02-27\'" +  " AND locality = 'University of San Francisco'";
//                locationStmt.executeUpdate(updateLocationSQL);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        };

//        executorService.scheduleAtFixedRate(task, 1,  1, TimeUnit.SECONDS);

//        Callable<Integer> task = () -> {
//            Connection connection = dataSource.getConnection();
//            Statement locationStmt = connection.createStatement();
//            String cleansingQuery = "DELETE FROM sanfrancisco WHERE time < " + "\'2018-02-26\'" +  " AND locality = 'University of San Francisco'";
//            System.out.println(cleansingQuery);
//            String updateLocationSQL = cleansingQuery;
//            locationStmt.executeUpdate(updateLocationSQL);
//            return null;
//        };
//
//        int delay = 5;
//        Future<Integer> result = scheduler.schedule(task, delay, TimeUnit.SECONDS);
//
//        try {
//            Integer value = result.get();
//            System.out.println("value = " + value);
//        } catch (InterruptedException | ExecutionException ex) {
//            ex.printStackTrace();
//        }

        //Add Random locations to table
//

//        Runnable task2 = new LocationCleanser();
//        int initialDelay = 1;
//        int periodDelay = 1;


//      System.setProperty("sqlite4java.library.path", "/Users/nathannguyen/Documents/Code/sqlite4java");
//
////      accountsDDB = DynamoDBEmbedded.create().amazonDynamoDB();
////      californiaDDB = DynamoDBEmbedded.create().amazonDynamoDB();
////      accountsDDB = null;
////      californiaDDB = null;
//    //    AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-west-2").build();
//
//      String usernameAttribute = "username";
//
//      ProvisionedThroughput provisionedthroughput = new ProvisionedThroughput(1000L, 1000L);
//
//      List<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>() {{
//          add(new KeySchemaElement()
//                  .withAttributeName(usernameAttribute)
//                  .withKeyType(KeyType.HASH));
//      }};
//
//      List<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>() {
//      {
//          add(new AttributeDefinition()
//                  .withAttributeName(usernameAttribute)
//                  .withAttributeType("S"));
//      }};
//
//      CreateTableRequest createTableRequest = new CreateTableRequest()
//              .withTableName("Accounts")
//              .withKeySchema(keySchema)
//              .withAttributeDefinitions(attributeDefinitions)
//              .withProvisionedThroughput(new ProvisionedThroughput()
//                      .withReadCapacityUnits(5L)
//                .withWriteCapacityUnits(6L));
//
//        List<KeySchemaElement> keySchema1 = new ArrayList<KeySchemaElement>() {{
//            add(new KeySchemaElement()
//                    .withAttributeName(usernameAttribute)
//                    .withKeyType(KeyType.HASH));
//        }};
//
//        List<AttributeDefinition> attributeDefinitions1 = new ArrayList<AttributeDefinition>() {
//            {
//                add(new AttributeDefinition()
//                        .withAttributeName(usernameAttribute)
//                        .withAttributeType("S"));
//            }};
//
//        CreateTableRequest createTableRequest1 = new CreateTableRequest()
//                .withTableName("California")
//                .withKeySchema(keySchema1)
//                .withAttributeDefinitions(attributeDefinitions1)
//                .withProvisionedThroughput(new ProvisionedThroughput()
//                        .withReadCapacityUnits(5L)
//                        .withWriteCapacityUnits(6L));
//
////      accountsDDB.createTable(createTableRequest);
////      californiaDDB.createTable(createTableRequest1);
//
//      HashMap BillyAttributes = new HashMap() {{
//            put("FirstName", new AttributeValue().withS("Billy"));
//            put("Username", new AttributeValue().withS("tester"));
//            put("Password", new AttributeValue().withS("Test"));
//      }};

//      PutRequest Billy = new PutRequest()
//              .withItem(BillyAttributes);

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

//      for (int i = 0; i < 100; i++) {
//          int finalI = i;
//          accountsDDB.putItem(new PutItemRequest()
//                  .withTableName("Accounts")
//                  .withItem(new HashMap() {{
//                      put("FirstName", new AttributeValue().withS(randomIdentifier()));
//                      put("Locality", new AttributeValue().withS("Nike"));
//                      put("username", new AttributeValue().withS("tester" + finalI));
//                      put("facebookId", new AttributeValue().withS("12345678"));
//                      put("friends", new AttributeValue().withSS("Random1", "Random2"));
//                      put("friendRequests", new AttributeValue().withSS("none"));
//                      put("sex", new AttributeValue().withS(String.valueOf(Sex.MALE)));
//                  }}));
//      }

//      accountsDDB.putItem(new PutItemRequest()
//              .withTableName("Accounts")
//              .withItem(new HashMap() {{
//                  put("FirstName", new AttributeValue().withS("Henry"));
//                  put("Locality", new AttributeValue().withS("Nike"));
//                  put("username", new AttributeValue().withS("Bob"));
//                  put("friends", new AttributeValue().withSS("Nathan", "Bob"));
//                  put("friendRequests", new AttributeValue().withSS("none"));
//                  put("sex", new AttributeValue().withS(String.valueOf(Sex.MALE)));
//              }}));


//      californiaDDB.putItem(new PutItemRequest()
//            .withTableName("California")
//            .withItem(new HashMap() {{
//                put("")
//            }}));

//        MongoClientURI uri = new MongoClientURI(
//            "mongodb+srv://kay:testerpassword@cluster0.mongodb.net/");
//
//        MongoClient mongoClient = new MongoClient(uri);
//
//        MongoDatabase database = mongoClient.getDatabase("test");

//        ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
//        PoolingNHttpClientConnectionManager cm =
//                new PoolingNHttpClientConnectionManager(ioReactor);
//        CloseableHttpAsyncClient client =
//                HttpAsyncClients.custom().setConnectionManager(cm).build();
//        client.start();
//
//        String[] toGet = {
//                "https://fathomless-gorge-73815.herokuapp.com/updateLocation",
//                "https://fathomless-gorge-73815.herokuapp.com/updateLocation"
//                "https://fathomless-gorge-73815.herokuapp.com/updateLocation",
//        };
//
//        GetThread[] threads = new GetThread[toGet.length];
//        for (int i = 0; i < threads.length; i++) {
//            HttpGet request = new HttpGet(toGet[i]);
//            threads[i] = new GetThread(client, request);
//        }
//
//        for (GetThread thread : threads) {
//            thread.start();
//        }
//
//        for (GetThread thread : threads) {
//            thread.join();
//        }

//        Future<HttpResponse<JsonNode>> future = Unirest.post("https://fathomless-gorge-73815.herokuapp.com/updateLocation")
//                .header("accept", "application/json")
//                .asJsonAsync(new Callback<JsonNode>() {
//                    @Override
//                    public void completed(HttpResponse<JsonNode> httpResponse) {
//
//                    }
//
//                    @Override
//                    public void failed(UnirestException e) {
//
//                    }
//
//                    @Override
//                    public void cancelled() {
//
//                    }
//                });

//        SanFranciscoTag sanFranciscoTag = new SanFranciscoTag(
//                "1",
//                "University of San Francisco",
//                now()
//        );
//        insertLocationTag(sanFranciscoTag);

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

}

enum Sex {
    MALE, FEMALE;
}
