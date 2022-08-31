package com.sxjkwm.pm.configuration;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Vic.Chu
 * @date 2022/8/6 9:07
 */
@Configuration
@PropertySource("classpath:config/esConfig/esConfig-${spring.profiles.active}.properties")
public class ElasticSearchConfig {

    @Value("${ipAddress:127.0.0.1}")
    private String ipAddress;

    @Value("${port:9200}")
    private String port;

    @Value("${scheme:http}")
    private String scheme;
    @Bean
    public RestHighLevelClient restHighLevelClient(){
        RestHighLevelClient client=new RestHighLevelClient(
                RestClient.builder(new HttpHost(ipAddress, Integer.parseInt(port), scheme)));
        return client;
    }

}
