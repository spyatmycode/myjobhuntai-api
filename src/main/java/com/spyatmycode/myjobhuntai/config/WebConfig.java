package com.spyatmycode.myjobhuntai.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    String[] origins = {};
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply to ALL endpoints
                // .allowedOrigins(origins) // React (Create-React-App or Vite)
                .allowedOriginPatterns("*") //TODO:remove this on production 
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow these HTTP verbs
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow cookies/auth headers if needed
    }
}
