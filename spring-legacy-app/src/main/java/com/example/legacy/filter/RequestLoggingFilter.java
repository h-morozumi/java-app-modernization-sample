package com.example.legacy.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Request logging filter using javax.servlet.Filter.
 *
 * ⚠ Migration Issue (Spring Boot 3):
 *   - javax.servlet.* → jakarta.servlet.*
 */
public class RequestLoggingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("RequestLoggingFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        long start = System.currentTimeMillis();
        HttpServletRequest req = (HttpServletRequest) request;

        chain.doFilter(request, response);

        long elapsed = System.currentTimeMillis() - start;
        HttpServletResponse res = (HttpServletResponse) response;
        log.info("Request: {} {} → {} ({}ms)",
                req.getMethod(), req.getRequestURI(), res.getStatus(), elapsed);
    }

    @Override
    public void destroy() {
        log.info("RequestLoggingFilter destroyed");
    }
}
