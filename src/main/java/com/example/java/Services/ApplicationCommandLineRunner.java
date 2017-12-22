package com.example.java.Services;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import com.amazonaws.services.dynamodbv2.model.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ApplicationCommandLineRunner implements CommandLineRunner {


  @Override
    public void run(String... strings) throws Exception {
        System.setProperty("sqlite4java.library.path", "/Users/nathannguyen/Documents/Code/sqlite4java");

        AmazonDynamoDB ddb = DynamoDBEmbedded.create().amazonDynamoDB();
        try {
            String tableName = "Movies";
            String hashKeyName = "film_id";
            CreateTableResult res = createTable(ddb, tableName, hashKeyName);

//        String tableName2 = "Movies";
//        String hashKeyName2 = "film_id";
//        CreateTableResult res2 = createTable(ddb, tableName2, hashKeyName2);

            TableDescription tableDesc = res.getTableDescription();

            ListTablesResult tables = ddb.listTables();

            // Create an in-memory and in-process instance of DynamoDB Local that skips HTTP
            // use the DynamoDB API with DynamoDBEmbedded
            listTables(ddb.listTables(), "DynamoDB Embedded");
        } finally {
            // Shutdown the thread pools in DynamoDB Local / Embedded
            if(ddb != null) {
                ddb.shutdown();
            }
        }

        // Create an in-memory and in-process instance of DynamoDB Local that runs over HTTP
        final String[] localArgs = { "-inMemory" };
        DynamoDBProxyServer server = null;
        try {
            server = ServerRunner.createServerFromCommandLineArgs(localArgs);
            server.start();

            ddb = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
                    // we can use any region here
                    new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
                    .build();

            // use the DynamoDB API over HTTP
            listTables(ddb.listTables(), "DynamoDB Local over HTTP");
        } finally {
            // Stop the DynamoDB Local endpoint
            if(server != null) {
                server.stop();
            }
        }

    }

    private static CreateTableResult createTable(AmazonDynamoDB ddb, String tableName, String hashKeyName) {
        List<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();
        attributeDefinitions.add(new AttributeDefinition(hashKeyName, ScalarAttributeType.S));

        List<KeySchemaElement> ks = new ArrayList<KeySchemaElement>();
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
