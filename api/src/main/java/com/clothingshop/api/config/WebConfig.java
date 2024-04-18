package com.clothingshop.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.customer.frontend.url}")
    private String customerFrontendUrl;
    @Value("${app.admin.frontend.url}")
    private String adminFrontendUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins(customerFrontendUrl, adminFrontendUrl)
                .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE");
    }
}
