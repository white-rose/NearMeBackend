package com.SmallTalk.LocationHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

@Component
public class LocationController implements LocationCleanser {

    @Autowired
    DataSource dataSource;

    @Override
    @Scheduled(fixedRate = 5000)
    public void cleanse() {

        try {
            Connection connection = dataSource.getConnection();
            Statement locationStmt = connection.createStatement();
            LocalDate today = LocalDate.now();
            String yesterday = today.minusDays(1).toString();

            String updateLocationSQL = "DELETE FROM sanfrancisco WHERE time < '" + yesterday +  "'; ";
            locationStmt.executeUpdate(updateLocationSQL);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
