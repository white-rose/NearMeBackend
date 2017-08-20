/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.java.Services;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import model.Greeting;
import model.Suggestion;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class HelloworldApplication {

  private static final String template = "The device token is , %s!";
  private final AtomicLong counter = new AtomicLong();

  @RequestMapping(value = "/" , method=RequestMethod.GET, produces = "application/json")
  public String home() {
    return "This is the default path";
  }

  /**
   * <a href="https://cloud.google.com/appengine/docs/flexible/java/how-instances-are-managed#health_checking">
   * App Engine health checking</a> requires responding with 200 to {@code /_ah/health}.
   */
  @RequestMapping("/_ah/health")
  public String healthy() {
    // Message body required though ignored
    return "Still surviving.";
  }

  @RequestMapping("/greeting")
  public Greeting greeting(@RequestParam(value="name", defaultValue = "Nathan") String name) {
    return new Greeting(counter.incrementAndGet(), String.format(template, name));
  }

  /*
  Nathan Iphone 7 device token - 847748fb896318b4cf5ab22884c5046a002ad3206d3930ad24a8003c1b91b4a7
   */

  @RequestMapping("/requestResturant")
  //UserID as parameter?
  public Greeting notification(@RequestParam(value="token") String deviceToken) {
      ApnsService service = APNS.newService()
              .withCert("/Users/mrrobot/Code/getting-started-java/helloworld-springboot/src/main/resources/Certificates.p12", "Cabinboy23")
              .withSandboxDestination()
              .build();

      String payload = APNS.newPayload()
              .alertBody("Simple!")
              .alertTitle("Test 123")
              .build();

    deviceToken = "847748fb896318b4cf5ab22884c5046a002ad3206d3930ad24a8003c1b91b4a7";

    service.push(deviceToken, payload);

    return new Greeting(counter.incrementAndGet(), String.format(template, deviceToken));
  }


  @RequestMapping("/suggestions")
  public String getAllSuggestions (Model model) {
    model.addAttribute("suggestions", getSuggestions());
    return "jsonTemplate";
  }

  public List getSuggestions() {
    Suggestion suggestion1 = new Suggestion("test", "test");
    Suggestion suggestion2 = new Suggestion("test2", "test2");
    Suggestion suggestion3 = new Suggestion("test3", "test3");

    List<Suggestion> myList = Arrays.asList(suggestion1, suggestion2, suggestion3);

    return  myList;
  }


}
