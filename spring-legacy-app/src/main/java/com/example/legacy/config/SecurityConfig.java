package com.example.legacy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security configuration using WebSecurityConfigurerAdapter.
 *
 * ⚠ Migration Issues (Spring Boot 3 / Spring Security 6):
 *   - WebSecurityConfigurerAdapter is REMOVED
 *     → Must use SecurityFilterChain @Bean instead
 *   - antMatchers() is REMOVED
 *     → Use requestMatchers()
 *   - authorizeRequests() is DEPRECATED
 *     → Use authorizeHttpRequests()
 *   - .and() chaining style deprecated
 *     → Use lambda DSL: http.authorizeHttpRequests(auth -> auth...)
 */
@Configuration
@EnableWebSecurity
@SuppressWarnings("deprecation")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // ⚠ authorizeRequests() → authorizeHttpRequests() in Boot 3
        // ⚠ antMatchers() → requestMatchers() in Boot 3
        http
            .authorizeRequests()
                .antMatchers("/", "/home", "/api/**", "/sysinfo", "/users/**",
                             "/css/**", "/js/**", "/actuator/**", "/login", "/h2-console/**").permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            .and()
                .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/")
                    .permitAll()
            .and()
                .logout()
                    .permitAll()
            .and()
                .csrf().disable()
                .headers()
                    .frameOptions().sameOrigin(); // for H2 console
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
            .and()
            .withUser("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
