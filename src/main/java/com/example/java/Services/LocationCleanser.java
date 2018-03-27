package com.example.java.Services;

import org.springframework.scheduling.annotation.Scheduled;

public interface LocationCleanser {

    void viewHistory(String location);

    @Scheduled
    void cleanse();

}
