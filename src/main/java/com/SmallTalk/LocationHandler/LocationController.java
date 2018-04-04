package com.SmallTalk.LocationHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

@RestController
//@Component
public class LocationController implements LocationCleanser {

    @Autowired
    DataSource dataSource;

    @Override
    @Scheduled(fixedRate = 5000)
    public void cleanse() {

        try {
            Connection connection = dataSource.getConnection();
            Statement locationStmt = connection.createStatement();
            LocalDate today = LocalDate.now();
            String yesterday = today.minusDays(1).toString();

            String updateLocationSQL = "DELETE FROM sanfrancisco WHERE time < '" + yesterday +  "'; ";
            locationStmt.executeUpdate(updateLocationSQL);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void AWSDynamoDBQuery() {

//          ApplicationCommandLineRunner.accountsDDB.putItem(new PutItemRequest()
//          .withTableName("Accounts")
//          .withItem(new HashMap() {{
//              put("FirstName", new AttributeValue().withS(userAccount.getFirstName()));
//              put("Locality", new AttributeValue().withS(userAccount.getLocality()));
//              put("username", new AttributeValue().withS(userAccount.getUsername()));
//              put("facebookid", new AttributeValue().withS(userAccount.getFacebookId()));
//              put("friends", new AttributeValue().withSS("Nathan", "Billy"));
//              put("friendRequests", new AttributeValue().withSS("none"));
//          }}));

    }

}
