package com.SmallTalk;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ActuatorController {

  private static final String template = "The device token is , %s!";
  private final AtomicLong counter = new AtomicLong();

  /**
   * <a href="https://cloud.google.com/appengine/docs/flexible/java/how-instances-are-managed#health_checking">
   * App Engine health checking</a> requires responding with 200 to {@code /_ah/health}.
   */
  @RequestMapping("/health")
  public String healthy() {
    // Message body required though ignored
    return "Still surviving.";
  }


  //Send Push Notication to phone
  @RequestMapping("/samplePush")
  public void notification(@RequestParam(value="token") String deviceToken) {
    ApnsService service = APNS.newService()
            .withCert("/Users/nathannguyen/Documents/Code/NearMeBackend/src/main/resources/Certificates.p12", "Cabinboy23")
            .withSandboxDestination()
            .build();

    String payload = APNS.newPayload()
            .alertBody("Simple!")
            .alertTitle("Test 123")
            .build();

    deviceToken = "c8ad4e8b7a96943039b3ea89a6a5508bc6426953fdbadfeae06c970b28a495c0";

    service.push(deviceToken, payload);

  }

}
