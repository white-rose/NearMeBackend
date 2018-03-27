package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UserAccount extends Profile {

    @JsonProperty
    String facebookId;
    @JsonProperty
    String userName;
    @JsonProperty
    String firstName;
    @JsonProperty
    String locality;
    @JsonProperty
    String sex;
    @JsonProperty
    List friends;
    @JsonProperty
    List friendRequests;

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
}
