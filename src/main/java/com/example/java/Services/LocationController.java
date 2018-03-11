package com.example.java.Services;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static java.time.LocalDate.now;

@RestController
public class LocationController {

    @Autowired
    DataSource dataSource;

    @RequestMapping(
            value = "/updateLocation",
            method = { RequestMethod.POST })
    public void updateLocation (@RequestBody model.Account userAccount) throws SQLException {

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

        Connection connection = dataSource.getConnection();
        Statement locationStmt = connection.createStatement();
        String updateLocationSQL = "INSERT INTO sanfrancisco (facebookid, locality, time)"
                + "VALUES ('" + userAccount.getFacebookId() + "','" + userAccount.getLocality() + "','" + now() + "')";
        locationStmt.executeUpdate(updateLocationSQL);

    }

    @Scheduled(fixedRate = 5000)
    public void cleanse() {
        try {
            Connection connection = dataSource.getConnection();
            Statement locationStmt = connection.createStatement();
            String updateLocationSQL = "DELETE FROM sanfrancisco WHERE time < " + DateTime.now() +  " AND locality = 'University of San Francisco'";
            locationStmt.executeUpdate(updateLocationSQL);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
