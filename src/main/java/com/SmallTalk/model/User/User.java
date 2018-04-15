package com.SmallTalk.model.User;

import com.SmallTalk.model.Location.Building;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

//@DynamoDBTable(tableName = "Accounts")
@RestController
@Component
public class User {

    //@DynamoDBHashKey(attributeName = "username")
    //@Id
    @JsonProperty
    private String facebookId;
    @JsonProperty
    private String firstName;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private String school;
    @JsonProperty
    private String userName;
    @JsonProperty
    private String locality;
    @JsonProperty
    private String sex;
    @JsonProperty
    private List friends;
    @JsonProperty
    private List friendRequests;
    @JsonProperty
    private String email;
    @JsonProperty
    private Boolean online;
    @JsonProperty
    private String password;

    private Date birthday;
    private Building buildingOccupied;

    @Autowired
    DataSource dataSource;

    public User() {
    }

    public User(String facebookId, String firstName, String lastName) {

        this.facebookId = facebookId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @RequestMapping (
            value = "/createAccount",
            method = RequestMethod.POST
    )
    public void create (@RequestBody User account) throws SQLException {

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public List getFriends() {
        return friends;
    }

    public void setFriends(List friends) {
        this.friends = friends;
    }

    public List getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(List friendRequests) {
        this.friendRequests = friendRequests;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Building getBuildingOccupied() {
        return buildingOccupied;
    }

    public void setBuildingOccupied(Building buildingOccupied) {
        this.buildingOccupied = buildingOccupied;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
