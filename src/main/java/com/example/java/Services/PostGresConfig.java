package com.example.java.Services;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "com.example.java.Services")
public class PostGresConfig {


    @Configuration
    @PropertySource("classpath:application.properties")
    public class DatabaseConfig {

//        @Value("${spring.datasource.url}")
//        private String test;

        @Bean
        @Primary
        @ConfigurationProperties(prefix = "spring.datasource")
        public DataSource dataSource() {
            DataSource ds = DataSourceBuilder.create().build();
            return ds;
        }
    }

}
