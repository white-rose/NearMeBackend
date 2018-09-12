package com.SmallTalk;

//import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

@Configuration
public class PostgresConfig {


//    @Configuration
//    @PropertySource("classpath:application.properties")
//    public class DatabaseConfig {
//
//        @Bean
//        @Primary
//        @ConfigurationProperties(prefix = "spring.datasource")
//        public DataSource dataSource() {
//            DataSource ds = DataSourceBuilder.create().build();
//            return ds;
//        }
//    }

}