package com.SmallTalk.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

//@DynamoDBTable(tableName = "Accounts")
//@Entity
public class UserAccount extends Account {

    //@DynamoDBHashKey(attributeName = "username")
    //@Id
    @JsonProperty
    String facebookId;
    @JsonProperty
    String userName;
    @JsonProperty
    String firstName;
    @JsonProperty
    String lastName;
    @JsonProperty
    String locality;
    @JsonProperty
    String sex;
    @JsonProperty
    List friends;
    @JsonProperty
    List friendRequests;
    @JsonProperty
    Boolean online;
    @JsonProperty
    String school;

//    public UserAccount(String facebookId, String firstName, String lastName) {
//        this.facebookId = facebookId;
//        this.firstName = firstName;
//        this.lastName = lastName;
//    }

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
}
