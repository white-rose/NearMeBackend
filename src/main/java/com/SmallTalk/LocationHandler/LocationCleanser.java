package com.SmallTalk.LocationHandler;

import org.springframework.scheduling.annotation.Scheduled;

public interface LocationCleanser {

  @Scheduled
  void cleanse();
}
