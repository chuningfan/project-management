package com.sxjkwm.pm.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Vic.Chu
 * @date 2022/7/4 15:43
 */
@Configuration
@PropertySource("classpath:config/epConfig/epConfig-${spring.profiles.active}.properties")
public class EPlatformDataSourceConfig {

    @Value("${ep.ds.address}")
    private String address;
    @Value("${ep.ds.port}")
    private String port;
    @Value("${ep.ds.username}")
    private String username;
    @Value("${ep.ds.password}")
    private String password;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

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

    @Bean("epJdbcTemplate")
    public JdbcTemplate epJdbcTemplate() {
        String url = "jdbc:mysql://" + address + ":" + port + "?autoReconnect=true";
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(5);
        config.setMaxLifetime(1800000);
        HikariDataSource hikariDataSource = new HikariDataSource(config);
        JdbcTemplate epJdbcTemplate = new JdbcTemplate(hikariDataSource);
        return epJdbcTemplate;
    }

}
