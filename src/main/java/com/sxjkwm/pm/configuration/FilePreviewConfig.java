package com.sxjkwm.pm.configuration;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.net.ConnectException;

/**
 * @author Vic.Chu
 * @date 2022/5/28 7:46
 */
@Configuration
@PropertySource("classpath:FilePreviewConfig.properties")
public class FilePreviewConfig {
    @Value("${openoffice.ip: localhost}")
    private String ip;

    @Value("${openoffice.port: 8081}")
    private String port;

    @Bean
    public OpenOfficeConnection openOfficeConnection() throws ConnectException {
        OpenOfficeConnection connection = new SocketOpenOfficeConnection(ip, Integer.valueOf(port));
        return connection;
    }

    @Bean
    public DocumentConverter documentConverter() throws ConnectException {
        OpenOfficeConnection connection = openOfficeConnection();
        connection.connect();
        DocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection);
        return converter;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
