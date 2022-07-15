package com.sxjkwm.pm.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author Vic.Chu
 * @date 2022/7/13 18:37
 */
@Configuration
public class JdbcTemplateConfig {

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Bean("jdbcTemplate")
    public JdbcTemplate jdbcTemplate() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(5);
        config.setMaxLifetime(1800000);
        HikariDataSource hikariDataSource = new HikariDataSource(config);
//        DataSource dataSource = DataSourceBuilder.create()
//                .driverClassName("com.mysql.cj.jdbc.Driver")
//                .username(username)
//                .password(password)
//                .url(url).build();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(hikariDataSource);
        return jdbcTemplate;
    }

}
