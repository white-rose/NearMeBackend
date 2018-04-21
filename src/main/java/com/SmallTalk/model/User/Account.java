package com.SmallTalk.model.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Account {

    @JsonProperty
    private Number userId;
    @JsonProperty
    private String username;
    @JsonProperty
    private String password;
    @JsonProperty
    private User user;

    @Autowired
    DataSource dataSource;

    public Account(Number userId, String username, String password, User user) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.user = user;
    }


    @RequestMapping(
            value = "/createAccount",
            method = RequestMethod.POST
    )
    public void createAccount (@RequestBody User account) throws SQLException {

        Connection connection = dataSource.getConnection();
        Statement createAccount = connection.createStatement();

        String createAccountQuery =
                "INSERT INTO accounts (username, firstname, lastname, password) VALUES ("

                        + "'" + account.getUserName() + "',"
                        + "'" + account.getFirstName() + "',"
                        + "'" + account.getLastName() + "',"
                        + "'" + account.getPassword() + "');";

        createAccount.executeUpdate(createAccountQuery);

    }

    public Number getUserId() {
        return userId;
    }

    public void setUserId(Number userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
