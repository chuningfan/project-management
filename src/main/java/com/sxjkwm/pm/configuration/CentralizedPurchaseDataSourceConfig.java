package com.sxjkwm.pm.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author Vic.Chu
 * @date 2022/7/4 15:43
 */
@Configuration
@PropertySource("classpath:CentralizedPurchaseDataSourceConfig.properties")
public class CentralizedPurchaseDataSourceConfig {

    @Value("${cp.ds.address}")
    private String address;
    @Value("${cp.ds.port}")
    private String port;
    @Value("${cp.ds.username}")
    private String username;
    @Value("${cp.ds.password}")
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

    @Bean("cpJdbcTemplate")
    public JdbcTemplate cpJdbcTemplate() {
        String url = "jdbc:mysql://" + address + ":" + port;
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
        JdbcTemplate cpJdbcTemplate = new JdbcTemplate(hikariDataSource);
        return cpJdbcTemplate;
    }

}
