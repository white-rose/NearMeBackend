package com.example.java.Services;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
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
      String hashKeyName = "username";

      List<KeySchemaElement> ks = new ArrayList<>();
      ks.add(new KeySchemaElement(hashKeyName, KeyType.HASH));

      ProvisionedThroughput provisionedthroughput = new ProvisionedThroughput(1000L, 1000L);

      ArrayList<KeySchemaElement> keySchema = new ArrayList<>();
      keySchema.add(new KeySchemaElement()
              .withAttributeName(hashKeyName)
              .withKeyType(KeyType.HASH));

      ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<>();
      attributeDefinitions.add(new AttributeDefinition()
              .withAttributeName(hashKeyName)
              .withAttributeType("S"));

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

      ListTablesResult listTablesResult = client.listTables();
      System.out.println(listTablesResult.getTableNames());

      ArrayList allWriteRequests = new ArrayList() {{
//          add(new WriteRequest().withPutRequest(Billy));
          add(new WriteRequest().withPutRequest(James));
          add(new WriteRequest().withPutRequest(Sam));
          add(new WriteRequest().withPutRequest(Sally));
          add(new WriteRequest().withPutRequest(Bob));
      }};

      allWriteRequests.add(new WriteRequest());
      HashMap moreItems = new HashMap() {{
          put("String", allWriteRequests);
      }};
      BatchWriteItemRequest populateItems = new BatchWriteItemRequest()
              .withRequestItems(moreItems);

//    client.batchWriteItem(populateItems);
//      client.putItem(new PutItemRequest()
//              .withTableName("Accounts")
//              .withItem(new HashMap() {{
//                  put("FirstName", new AttributeValue().withS("Bob"));
//                  put("Username", new AttributeValue().withS("tester3"));
//                  put("Password", new AttributeValue().withS("test"));
//              }}));

      DescribeTableResult describeTableResult = client.describeTable("Accounts");
      System.out.println("Describing table is: " + describeTableResult.toString());

      ScanResult allResults = client.scan("Accounts", Arrays.asList("Username", "FirstName"));
      allResults.getItems().forEach(item -> System.out.println("This item has: " + item.get("FirstName") + "\n"));
      allResults.getItems().forEach(item -> System.out.println("This item has: " + item.get("Username") + "\n"));
      AttributeValue firstNameAttribute = allResults.getItems().get(0).get("FirstName");
      System.out.println("The official first name is : " + firstNameAttribute.getS());

  }

    private static void createTable(
            String tableName, long readCapacityUnits, long writeCapacityUnits,
            String hashKeyName, String hashKeyType) {

        createTable(tableName, readCapacityUnits, writeCapacityUnits,
                hashKeyName, hashKeyType, null, null);
    }

    private static void createTable(String tableName, long readCapacityUnits,
                                    long writeCapacityUnits, String hashKeyName, String hashKeyType,
                                    String rangeKeyName, String rangeKeyType) {

            ArrayList<KeySchemaElement> keySchema = new ArrayList<>();
            keySchema.add(new KeySchemaElement()
                .withAttributeName(hashKeyName)
                .withKeyType(KeyType.HASH));

            ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<>();
            attributeDefinitions.add(new AttributeDefinition()
                .withAttributeName(hashKeyName)
                .withAttributeType(hashKeyType));

            if (rangeKeyName != null) {
                keySchema.add(new KeySchemaElement()
                    .withAttributeName(rangeKeyName)
                    .withKeyType(KeyType.RANGE));
                attributeDefinitions.add(new AttributeDefinition()
                    .withAttributeName(rangeKeyName)
                    .withAttributeType(rangeKeyType));
            }

            CreateTableRequest request = new CreateTableRequest()
                    .withTableName(tableName)
                    .withKeySchema(keySchema)
                    .withProvisionedThroughput( new ProvisionedThroughput()
                        .withReadCapacityUnits(readCapacityUnits)
                        .withWriteCapacityUnits(writeCapacityUnits));



            request.setAttributeDefinitions(attributeDefinitions);

            System.out.println("Issuing CreateTable requestfor " + tableName);
            // CreateTableResult table = dynamoDB.createTable(request);
            System.out.println("Waiting for " + tableName
                    + " to be created...this may take a while...");
            //table.waitForActive();


    }

    private static CreateTableResult createTable(AmazonDynamoDB ddb, String tableName, String hashKeyName) {
        List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
        attributeDefinitions.add(new AttributeDefinition(hashKeyName, ScalarAttributeType.S));

        List<KeySchemaElement> ks = new ArrayList<>();
        ks.add(new KeySchemaElement(hashKeyName, KeyType.HASH));

        ProvisionedThroughput provisionedthroughput = new ProvisionedThroughput(1000L, 1000L);

        CreateTableRequest request =
                new CreateTableRequest()
                        .withTableName(tableName)
                        .withAttributeDefinitions(attributeDefinitions)
                        .withKeySchema(ks)
                        .withProvisionedThroughput(provisionedthroughput);

        return ddb.createTable(request);
    }

    public static void listTables(ListTablesResult result, String method) {
        System.out.println("found " + Integer.toString(result.getTableNames().size()) + " tables with " + method);
        for(String table : result.getTableNames()) {
            System.out.println(table);
        }
    }
}
