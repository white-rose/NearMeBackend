package com.example.java.Services;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class ApplicationCommandLineRunner implements CommandLineRunner {

  @Override
    public void run(String... strings) throws Exception {
      System.setProperty("sqlite4java.library.path", "/Users/nathannguyen/Documents/Code/sqlite4java");

//    AmazonDynamoDB ddb = DynamoDBEmbedded.create().amazonDynamoDB();
      AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-west-2").build();

      String tableName = "Users";
      String firstNameAttribute = "FirstName";
      String lastNameAttribute = "LastName";
      String localityAttribute = "Locality";
      String usernameAttribute = "username";


      ProvisionedThroughput provisionedthroughput = new ProvisionedThroughput(1000L, 1000L);

      List<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>() {{
//          add(new KeySchemaElement()
//                  .withAttributeName(localityAttribute)
//                  .withKeyType(KeyType.HASH));
          add(new KeySchemaElement()
                  .withAttributeName(usernameAttribute)
                  .withKeyType(KeyType.HASH));
      }};

      List<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>() {
          {
//              add(new AttributeDefinition()
//                      .withAttributeName(firstNameAttribute)
//                      .withAttributeType("S"));
//              add(new AttributeDefinition()
//                      .withAttributeName(lastNameAttribute)
//                      .withAttributeType("S"));
//              add(new AttributeDefinition()
//                      .withAttributeName(localityAttribute)
//                      .withAttributeType("S"));
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

      ListTablesResult listTablesResult = client.listTables();
      System.out.println(listTablesResult.getTableNames());

      ArrayList allWriteRequests = new ArrayList() {{
//          add(new WriteRequest().withPutRequest(Billy));
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

      client.putItem(new PutItemRequest()
              .withTableName("Accounts")
              .withItem(new HashMap() {{
                  put("FirstName", new AttributeValue().withS("Greg"));
                  put("Locality", new AttributeValue().withS("Nike"));
                  put("username", new AttributeValue().withS("tester"));
              }}));

      DescribeTableResult describeTableResult = client.describeTable("Accounts");
      System.out.println("Describing table is: " + describeTableResult.toString());

//      allResults.getItems().forEach(item -> System.out.println("This item has username: " + item.get("username") + "\n"));
//      allResults.getItems().forEach(item -> System.out.println("This item has locality: " + item.get("locality") + "\n"));
//      allResults.getItems().forEach(item -> System.out.println("This item has firstname: " + item.get("FirstName") + "\n"));
//      allResults.getItems().forEach(item -> System.out.println("This item has: " + item.get("Username") + "\n"));
//      AttributeValue firstName = allResults.getItems().get(0).get("FirstName");
//      System.out.println("The official first name is : " + firstName.getS());

  }

    public static void listTables(ListTablesResult result, String method) {
        System.out.println("found " + Integer.toString(result.getTableNames().size()) + " tables with " + method);
        for(String table : result.getTableNames()) {
            System.out.println(table);
        }
    }
}
