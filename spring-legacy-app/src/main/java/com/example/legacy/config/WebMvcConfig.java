package com.example.legacy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Web MVC configuration using WebMvcConfigurerAdapter.
 *
 * ⚠ Migration Issues (Spring Boot 3 / Spring 6):
 *   - WebMvcConfigurerAdapter is REMOVED
 *     → Must implement WebMvcConfigurer interface directly
 *   - Trailing slash matching is DISABLED by default in Boot 3
 *     → /users/ no longer matches /users
 */
@Configuration
@SuppressWarnings("deprecation")
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/about").setViewName("index");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // ⚠ In Boot 3, allowedOrigins("*") cannot be combined with allowCredentials(true)
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
