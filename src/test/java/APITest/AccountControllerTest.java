package APITest;

import com.SmallTalk.AccountHandler.AccountController;
import com.SmallTalk.AccountHandler.AccountService;
import com.SmallTalk.LocationHandler.LocationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountController.class)
@AutoConfigureMockMvc
public class AccountControllerTest {

   @Autowired
   MockMvc mvc;

   @MockBean
   private AccountService accountService;

   @MockBean
   private LocationService locationService;

   @Test
   public void testPullingNearbyUsers() throws Exception {

       mvc.perform(post("/pullNearbyUsers")
               .param("locality", String.valueOf("SanFrancisco")))
               .andExpect(status().isOk());

   }

}
