package com.chrisgya.tryout.config;


import com.chrisgya.tryout.config.properties.CustomDataSourceProperties;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.max.connection.pool.size}")
    private int maxPoolSize;

    @Primary
    @Bean(name = "demoDS")
    public DataSource datasource(CustomDataSourceProperties customDataSourceProperties) {
        final HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName(customDataSourceProperties.getDriverClassName());
        ds.setJdbcUrl(customDataSourceProperties.getUrl());
        ds.setUsername(customDataSourceProperties.getUsername());
        ds.setPassword(customDataSourceProperties.getPassword());
        ds.setMaximumPoolSize(maxPoolSize);
        return ds;
    }

}
