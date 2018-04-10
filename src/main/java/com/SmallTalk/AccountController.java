package com.SmallTalk;

import com.SmallTalk.model.Location.Building;
import com.SmallTalk.model.User.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@Component
public class AccountController {

    @Autowired
    DataSource dataSource;

    private Logger logger = LoggerFactory.getLogger(AccountController.class);

    //Heroku Tables
    static final String accounts = "accounts";
    static final String sanfrancisco = "sanfrancisco";

    public void create (User account) throws SQLException {

        Connection connection = dataSource.getConnection();
        Statement createAccount = connection.createStatement();

        String createAccountQuery =
                "INSERT INTO accounts (firstname, lastname, facebookid, email, school) VALUES ("
                        + "'" + account.getFirstName() + "',"
                        + "'" + account.getLastName() + "',"
                        + "'" + account.getFacebookId() + "',"
                        + "'" + account.getEmail() + "',"
                        + "'" + account.getSchool() + "');";

        createAccount.executeUpdate(createAccountQuery);

    }

    @RequestMapping(
            value = "/pullAccounts",
            method = RequestMethod.POST)
    private List<User> pullAllNearbyUsers (@RequestBody User currentAccount) throws SQLException {

        List<User> users = new ArrayList<>();

        long beginningTime = System.currentTimeMillis();
        Statement stmt = dataSource.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(
                "SELECT DISTINCT accounts.firstname, accounts.lastname, accounts.facebookid, accounts.school " +
                "FROM accounts " +
                "inner join sanfrancisco " +
                "on accounts.facebookid=sanfrancisco.facebookid " +
                "WHERE locality = '" + currentAccount.getLocality() +
                "' AND time <= '" + LocalDate.now() + "' " +
                "AND ONLINE = true");

        while (rs.next()) {
            User user = new User();
            user.setFacebookId(rs.getString("facebookid"));
            user.setFirstName(rs.getString("firstname"));
            user.setLastName(rs.getString("lastname"));
            user.setSchool(rs.getString("school"));
            users.add(user);
        }

        Building userBuilding = currentAccount.getBuildingOccupied();

        if (users.size() > userBuilding.maxCapacity)
            System.out.println(userBuilding.name + " has exceeded maximum capacity");

        long endTime = System.currentTimeMillis();
        System.out.println("Time to pull nearby users " + (endTime - beginningTime) + " milliseconds");

        logger.info(users.size() + " are occupying " + currentAccount.getLocality());

        return users;

    }

    @RequestMapping(
            value = "/updateOnlineStatus",
            method = { RequestMethod.PUT })
    private void updateOnlineStatus (@RequestBody User user) throws SQLException {

        System.out.println(!user.getOnline());

        Connection connection = dataSource.getConnection();
        String updateOnlineStatusQuery = "UPDATE accounts " +
                "SET online = '" + user.getOnline() + "' " +
                "WHERE facebookid = '" + user.getFacebookId() + "';";
        Statement onlineStatement = connection.createStatement();
        onlineStatement.executeUpdate(updateOnlineStatusQuery);
    }

}
