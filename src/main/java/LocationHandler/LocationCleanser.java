package LocationHandler;

import org.springframework.scheduling.annotation.Scheduled;

public interface LocationCleanser {

    void viewHistory(String location);

    @Scheduled
    void cleanse();

}
