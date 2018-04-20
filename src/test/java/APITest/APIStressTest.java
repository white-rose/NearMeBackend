package APITest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class APIStressTest {

    @Test
    public void testAccoutCreation() {



    }

    @Test
    public void testAddPullOfAccounts() {

        try {
            HttpResponse<String> response = Unirest.post("https://crystal-smalltalk.herokuapp.com/pullAccounts")
                    .header("Content-Type", "application/json")
                    .header("Cache-Control", "no-cache")
                    .header("Postman-Token", "4dd3a759-22b9-492d-8f1e-6f82c1505703")
                    .body("{\n\t\"firstname\": \"Nathan\",\n\t\"username\": \"Tester\",\n\t\"locality\": \"Union Square\",\n\t\"sex\": \"Male\",\n\t\"friends\": [\"test\", \"test\"],\n\t\"facebookId\": \"987654321\"\n}")
                    .asString();
            System.out.println(response.getBody());
        } catch (UnirestException ex) {
            System.out.println(ex.getMessage());
        }

    }

    /* Testing Facebook API to get facebook photo */
    @Test
    public void testFacebookPhotoCall () {

        try {
            HttpResponse<String> response = Unirest.get("http://graph.facebook.com/4/picture?type=large")
                    .header("Cache-Control", "no-cache")
                    .header("Postman-Token", "bade8763-e252-437c-bcbd-1204be05ec3f")
                    .asString();

            Assert.assertEquals(200, response.getStatus());
        } catch (UnirestException ex) {
            System.out.println(ex.getMessage());
        }

    }

}
