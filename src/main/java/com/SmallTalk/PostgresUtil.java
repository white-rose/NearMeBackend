package com.SmallTalk;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Statement;

@RestController
@Component
public class PostgresUtil {

    @Autowired
    DataSource dataSource;

    Statement statement;

    @Autowired
    public Statement openPostgresReference() {
        if (statement == null) {
            try {
                return dataSource.getConnection().createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            return statement;
        }

        return null;
    }

}
