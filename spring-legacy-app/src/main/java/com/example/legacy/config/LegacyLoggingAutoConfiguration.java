package com.example.legacy.config;

import com.example.legacy.filter.RequestLoggingFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for legacy request logging.
 *
 * ⚠ Migration Issues (Spring Boot 3):
 *   - Registration via META-INF/spring.factories is REMOVED
 *     → Must use META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports
 *   - Class should use @AutoConfiguration instead of @Configuration
 *   - One class name per line in the imports file (no key=value format)
 */
@Configuration
@ConditionalOnProperty(name = "app.feature.request-logging", havingValue = "true", matchIfMissing = true)
public class LegacyLoggingAutoConfiguration {

    @Bean
    public FilterRegistrationBean<RequestLoggingFilter> requestLoggingFilter() {
        FilterRegistrationBean<RequestLoggingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestLoggingFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        return registration;
    }
}
