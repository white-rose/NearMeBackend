package model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Accounts")
public class Account {

    @DynamoDBHashKey(attributeName = "Username")
    private String username;
    @DynamoDBHashKey(attributeName = "FirstName")
    private String firstName;
    @DynamoDBHashKey(attributeName = "LastName")
    private String lastName;
    @DynamoDBHashKey(attributeName = "locality")
    private String locality;
    @DynamoDBHashKey(attributeName = "latitude")
    private Double latitude;
    @DynamoDBHashKey(attributeName = "longitude")
    private Double longitude;
    @DynamoDBHashKey(attributeName = "facebookId")
    private String facebookId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }
}
