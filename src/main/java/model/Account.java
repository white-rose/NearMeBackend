package model;

//@DynamoDBTable(tableName = "Accounts")
//@Entity
public class Account {

//    @DynamoDBHashKey(attributeName = "username")
//    @Id
    private String username;
//    @DynamoDBHashKey(attributeName = "firstname")
    private String firstName;
//    @DynamoDBHashKey(attributeName = "lastname")
    private String lastName;
//    @DynamoDBHashKey(attributeName = "locality")
    private String locality;
//    @DynamoDBHashKey(attributeName = "latitude")
    private Double latitude;
//    @DynamoDBHashKey(attributeName = "longitude")
    private Double longitude;
//    @DynamoDBHashKey(attributeName = "facebookId")
//    @Id
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
