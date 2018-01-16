package com.example.java.Services;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.model.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ApplicationCommandLineRunner implements CommandLineRunner {

    public static AmazonDynamoDB ddb;

    @Override
    public void run(String... strings) throws Exception {
      System.setProperty("sqlite4java.library.path", "/Users/nathannguyen/Documents/Code/sqlite4java");

      ddb = DynamoDBEmbedded.create().amazonDynamoDB();
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

      ddb.createTable(createTableRequest);

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
      PutRequest James = new PutRequest()
              .withItem(JamesAttribute);
      PutRequest Sam = new PutRequest()
              .withItem(SamAttributes);
      PutRequest Sally = new PutRequest()
              .withItem(SallyAttributes);
      PutRequest Bob = new PutRequest()
              .withItem(BobAttributes);
      PutRequest Greg = new PutRequest()
              .withItem(GregAttributes);
      PutRequest Michael = new PutRequest()
              .withItem(BobAttributes);
      PutRequest Elise = new PutRequest()
              .withItem(BobAttributes);

      ListTablesResult listTablesResult = ddb.listTables();
      System.out.println(listTablesResult.getTableNames());

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
    //    client.batchWriteItem(populateItems);

    //      for (int i = 0; i < 100; i++) {
    //          int finalI = i;
    //          ddb.putItem(new PutItemRequest()
    //                  .withTableName("Accounts")
    //                  .withItem(new HashMap() {{
    //                      put("FirstName", new AttributeValue().withS(randomIdentifier()));
    //                      put("Locality", new AttributeValue().withS("Nike"));
    //                      put("username", new AttributeValue().withS("tester" + finalI));
    //                      put("friends", new AttributeValue().withSS("Random1", "Random2"));
    //                      put("friendRequests", new AttributeValue().withSS("none"));
    //                  }}));
    //      }
    //
    //      ddb.putItem(new PutItemRequest()
    //              .withTableName("Accounts")
    //              .withItem(new HashMap() {{
    //                  put("FirstName", new AttributeValue().withS("Nathan"));
    //                  put("Locality", new AttributeValue().withS("Nike"));
    //                  put("username", new AttributeValue().withS("Nathan"));
    //                  put("friends", new AttributeValue().withSS("Nathan", "Billy"));
    //                  put("friendRequests", new AttributeValue().withSS("none"));
    //              }}));
    //
    //      ddb.putItem(new PutItemRequest()
    //              .withTableName("Accounts")
    //              .withItem(new HashMap() {{
    //                  put("FirstName", new AttributeValue().withS("Bob"));
    //                  put("Locality", new AttributeValue().withS("Nike"));
    //                  put("username", new AttributeValue().withS("Bob"));
    //                  put("friends", new AttributeValue().withSS("Bob", "Billy"));
    //                  put("friendRequests", new AttributeValue().withSS("none"));
    //              }}));

    //    DescribeTableResult describeTableResult = client.describeTable("Accounts");
    //    System.out.println("Describing table is: " + describeTableResult.toString());
      ScanResult allResults = ddb.scan("Accounts", Arrays.asList("FirstName", "Locality", "username"));

    //    allResults.getItems().forEach(item -> System.out.println("This item has firstname: " + item.get("FirstName") + "\n"));
      System.out.println(allResults.getItems());
    //    allResults.getItems().forEach(item -> System.out.println("This item has: " + item.get("Username") + "\n"));
    //    AttributeValue firstName = allResults.getItems().get(0).get("FirstName");
    //    System.out.println("The official first name is : " + firstName.getS());

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
