package com.example.java.Services;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "com.example.java.Services")
public class MainConfig {

//    @Bean
//    public BasicDataSource dataSource() throws URISyntaxException {
//        URI dbUri = new URI("postgres://yfdcultzhzhsos:08357ce1b8f56cb12bfd807e64a6bdf4d7f828bba48df33ec225518595f7fa85@ec2-54-225-230-142.compute-1.amazonaws.com:5432/dba8br45ci0k1e?sslmode=require");
//
//        String username = dbUri.getUserInfo().split(":")[0];
//        String password = dbUri.getUserInfo().split(":")[1];
//        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
//
//        BasicDataSource basicDataSource = new BasicDataSource();
//        basicDataSource.setUrl(dbUrl);
//        basicDataSource.setUsername(username);
//        basicDataSource.setPassword(password);
//
//        return basicDataSource;
//    }

    @Configuration
    public class DatabaseConfig {
        @Bean
        @Primary
        @ConfigurationProperties(prefix = "spring.datasource")
        public DataSource dataSource() {
            DataSource ds = DataSourceBuilder.create().build();
            return ds;
        }
    }

}
