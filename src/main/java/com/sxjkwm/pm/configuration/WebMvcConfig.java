package com.sxjkwm.pm.configuration;

import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.Iterator;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:favicon.ico");
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowCredentials(true);
        corsConfig.setMaxAge(3600L);
        addAttr(corsConfig, "origin", "*");
        addAttr(corsConfig, "method", "*");
        addAttr(corsConfig, "header", "*");
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", corsConfig);
        FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<>(new CorsFilter(configSource));
        corsBean.setName("crossOriginFilter");
        corsBean.setOrder(0);
        return corsBean;
    }

    private void addAttr(CorsConfiguration corsConfig, String key, String values) {
        if (StringUtils.isBlank(values) || CorsConfiguration.ALL.equals(values)) {
            switch (key) {
                case "origin": corsConfig.addAllowedOrigin(CorsConfiguration.ALL); break;
                case "header": corsConfig.addAllowedHeader(CorsConfiguration.ALL); break;
                case "method": corsConfig.addAllowedMethod(CorsConfiguration.ALL); break;
                default: break;
            }
        } else {
            Iterable<String> valItrable = Splitter.on(",").split(values);
            Iterator<String> itr = valItrable.iterator();
            String value;
            while (itr.hasNext()) {
                value = itr.next();
                switch (key) {
                    case "origin": corsConfig.addAllowedOrigin(value); break;
                    case "header": corsConfig.addAllowedHeader(value); break;
                    case "method": corsConfig.addAllowedMethod(value); break;
                    default: break;
                }
            }
        }
    }

}
