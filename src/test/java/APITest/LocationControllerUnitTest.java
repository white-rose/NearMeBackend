package APITest;

import com.SmallTalk.LocationHandler.LocationController;
import com.SmallTalk.LocationHandler.LocationService;
import com.SmallTalk.model.User.User;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LocationController.class)
@AutoConfigureMockMvc
public class LocationControllerUnitTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private LocationService service;

    @Test
    public void testTrackLocation() throws Exception {

        User user = new User();
        user.setUserName("User1");
        user.setLocality("Random");
        Double longitude = 11.1111, latitude = 12.121212;

        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(user);
        String jsonStringBody = "{ \"userName\": \"Tester\", \"locality\": \"test\"}";

        MockHttpServletRequestBuilder builder =
                post("/track")
                        .param("longitude", String.valueOf(longitude))
                        .param("latitude", String.valueOf(latitude))
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(jsonStringBody)
                        .characterEncoding("utf-8");

        mvc.perform(post("/track")
                    .param("longitude", String.valueOf(longitude))
                    .param("latitude", String.valueOf(latitude))
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
//                    .header("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE)
//                    .header("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                    .content(jsonBody))
                    .andExpect(status().isOk());

    }

    @Test
     public void testPost () {

        HttpUriRequest request = new HttpPost("/track");

    }


}
