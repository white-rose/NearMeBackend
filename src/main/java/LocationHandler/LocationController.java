package LocationHandler;

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
import java.time.LocalDate;

import static java.time.LocalDate.now;

@RestController
public class LocationController implements LocationCleanser {

    @Autowired
    DataSource dataSource;

    @RequestMapping(
            value = "/goOffline")
    public void goOffline (@RequestBody model.Account userAccount) {

        String onlineSql = "update accounts set firstname = 'Nathan' where facebookid = '10215179647429020'";

    }

    @RequestMapping(
            value = "/updateLocation",
            method = { RequestMethod.POST })
    public void updateLocation (@RequestBody model.Account userAccount) throws SQLException {

        //AWS Dynamo DB
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

    //TODO: delete day before today's date
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

    @Override
    public void viewHistory(String location) {}

}
