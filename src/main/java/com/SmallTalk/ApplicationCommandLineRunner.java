package com.SmallTalk;

import com.SmallTalk.model.Location.LocationTag;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;

@Component
public class ApplicationCommandLineRunner implements CommandLineRunner {

    @Autowired
    DataSource dataSource;

    @Override
    public void run(String... strings) throws Exception {

//        dummyAccountData();
//        dummyHistoryData();
//        getStats();
    }

    private void getStats () throws SQLException {

        HashMap<String, Integer> locations = new HashMap<>();

        Connection connection = dataSource.getConnection();
        Statement pullStatement = connection.createStatement();
        String pullAll = "select * from sanfrancisco";
        ResultSet rs = pullStatement.executeQuery(pullAll);

        int capacitiy = 1;
        while (rs.next()) {
            String location = rs.getString("locality");
            if (locations.containsKey(location)) {
                capacitiy = locations.get(location).intValue();
                capacitiy += 1;
                locations.put(location, capacitiy++);
            }
            else {
                locations.put(location, capacitiy);
            }
        }

        System.out.println(locations);

    }

    // Returns random String
    private String randomIdentifier() {
        // class variable
        final String lexicon = "abcdefghijklmnopqrstuvwxyz";

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

    private void deletion () throws SQLException{

        Connection connection = dataSource.getConnection();
        Statement deleteStatement = connection.createStatement();
        String deleteQuery = "delete from accounts where facebookid = '" + 1 + "'";

        ExecutorService executor = Executors.newWorkStealingPool();

        //Delete from table at the same time and see what happens
        List<Callable<?>> callableList = Arrays.asList(
                () -> {/*delete record from table*/ return null;},
                () -> {/*delete record from table*/ return null;},
                () -> {/*delete record from table*/ return null;}
        );

        for (Callable callable : callableList) {
            try {
                callable.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void dummyAccountData() {

            for (int i = 0; i < 100; i++) {
            try (Connection connection = dataSource.getConnection()) {
                Statement createDummyData = connection.createStatement();
                String insertQuery = "insert into accounts (username, lastname, firstname, facebookid, online, school) " +
                        "VALUES ('" + randomIdentifier() + "', '"+  randomIdentifier() + "','" + randomIdentifier() + "', " + i + " , true, 'University of Sanfrancisco');";
                String deleteQuery = "delete from accounts where facebookid = '" + i + "'";
                createDummyData.executeUpdate(insertQuery);

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

    private void dummyHistoryData()  {

        final String BRANNAN_APARTMENTS = "855 Brannan Apartments";
        final String UNIVERSITY_OF_SANFRACISCO = "University of San Francisco";
        final String UNION_SQUARE = "Union Square";

        List<String> allUsernames;

        try (Connection connection = dataSource.getConnection()) {
            Statement trackLocationStatement = connection.createStatement();
            allUsernames = getAllUserNames();
            for (String username : allUsernames) {
                String insertQuery = "INSERT INTO sanfrancisco (username, locality, time) VALUES ("
                        + "'" + username + "',"
                        + "'" + UNION_SQUARE + "',"
                        + "'" + LocalDate.now().toString() + "');";
                trackLocationStatement.executeUpdate(insertQuery);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }


    }

    public List getAllUserNames () throws SQLException {

        Connection connection = dataSource.getConnection();
        String pullAllUsernamesQuery = "select * from users";
        Statement usernameStatement = connection.createStatement();
        ResultSet result = usernameStatement.executeQuery(pullAllUsernamesQuery);

        ArrayList<String> allUsernames = new ArrayList();

        while (result.next()) {
            allUsernames.add(result.getString("username"));
        }

        return allUsernames;

    }

    private void multipleLocationUpdateRequests () {

        Future<HttpResponse<JsonNode>> future = Unirest.post("localhost:8080/samplePush?token=c8ad4e8b7a96943039b3ea89a6a5508bc6426953fdbadfeae06c970b28a495c0")
                .header("accept", "application/json")
                .asJsonAsync(new Callback<JsonNode>() {
                    @Override
                    public void completed(HttpResponse<JsonNode> httpResponse) {
                        System.out.print("Online status was able to update");
                    }

                    @Override
                    public void failed(UnirestException e) {
                        System.out.println("Online status update has failed");
                    }

                    @Override
                    public void cancelled() {
                        System.out.println("Online status update has cancelled");
                    }
                });

        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    /*
    );
    insertLocationTag(sanFranciscoTag);

    ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor();
    PoolingNHttpClientConnectionManager cm =
            new PoolingNHttpClientConnectionManager(ioReactor);
    CloseableHttpAsyncClient client =
            HttpAsyncClients.custom().setConnectionManager(cm).build();
    client.start();
    */
    }

    public void insertLocationTag(LocationTag locationTag) {

        Random rand = new Random();

        String SQL = "INSERT INTO sanfrancisco (facebookid, locality, time)"
                + "VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS)) {
             pstmt.setString(1, locationTag.getFacebookId());
             pstmt.setString(2, locationTag.getLocality());
             pstmt.setString(3, locationTag.getTimestamp().toString());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

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

}
