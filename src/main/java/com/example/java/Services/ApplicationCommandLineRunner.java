package com.example.java.Services;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;

public class ApplicationCommandLineRunner /*implements CommandLineRunner*/ {


    public static void listTables(ListTablesResult result, String method) {
        System.out.println("found " + Integer.toString(result.getTableNames().size()) + " tables with " + method);
        for (String table : result.getTableNames()) {
            System.out.println(table);
        }
    }

//  @Override
    public void run(String... strings) throws Exception {
        AmazonDynamoDB dynamoDB = null;

        try {
            dynamoDB = DynamoDBEmbedded.create().amazonDynamoDB();

            listTables(dynamoDB.listTables(), "DynamoDB Embedded");
        } finally {
            if (dynamoDB != null) {
                dynamoDB.shutdown();
            }
        }

        final String[] localArgs = { "-inMemory" };
        DynamoDBProxyServer server = null;

        try {
            server = ServerRunner.createServerFromCommandLineArgs(localArgs);
            server.start();

            dynamoDB = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(
                    new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "US East")).build();
            listTables(dynamoDB.listTables(), "DynamoDB Local over HTTP");
        } finally {
            if(server != null) {
                server.stop();
            }
        }
    }
}
