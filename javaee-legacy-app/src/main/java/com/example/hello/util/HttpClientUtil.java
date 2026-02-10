package com.example.hello.util;

import java.io.IOException;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * HTTP client utility using Apache HttpClient 5.x (updated from vulnerable 4.5.2).
 * CVE-2020-13956 fixed in version 5.x.
 */
public class HttpClientUtil {

    private static final Logger logger = LogManager.getLogger(HttpClientUtil.class);

    /**
     * Performs a simple HTTP GET request.
     */
    public static String get(String url) throws IOException {
        logger.info("HTTP GET: " + url);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                String body = EntityUtils.toString(response.getEntity());
                logger.info("Response status: " + response.getCode());
                return body;
            } catch (ParseException e) {
                logger.error("Failed to parse HTTP response", e);
                throw new IOException("Failed to parse HTTP response", e);
            }
        }
    }
}
