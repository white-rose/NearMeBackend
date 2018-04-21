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