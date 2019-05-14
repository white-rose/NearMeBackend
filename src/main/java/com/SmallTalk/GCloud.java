package com.SmallTalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/** Created by mrrobot on 7/5/17. */
@SpringBootApplication
@EnableScheduling
public class GCloud {

  public static void main(String[] args) {
    SpringApplication.run(GCloud.class, args);
  }
}
