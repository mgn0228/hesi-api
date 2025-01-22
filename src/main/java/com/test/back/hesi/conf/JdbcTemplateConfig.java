package com.test.back.hesi.conf;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class JdbcTemplateConfig {

    @Value("${spring.datasource.driver-class-name}")
    public String driver;

    @Value("${spring.datasource.url}")
    public String url;

    @Value("${spring.datasource.username}")
    public String username;

    @Value("${spring.datasource.password}")
    public String password;
    
    @Bean
    public BasicDataSource source() {
        BasicDataSource source = new BasicDataSource();
        source.setUrl(url);
        source.setDriverClassName(driver);
        source.setUsername(username);
        source.setPassword(password);

        return source;
    }

    @Bean
    public JdbcTemplate dataSource(BasicDataSource source) {
        return new JdbcTemplate(source);
    }
}
