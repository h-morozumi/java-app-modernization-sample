package com.example.hello.util;

import java.io.IOException;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * HTTP client utility using vulnerable Apache HttpClient 4.5.2.
 * CVE-2020-13956: Incorrect handling of malformed authority in URIs.
 */
public class HttpClientUtil {

    private static final Logger logger = Logger.getLogger(HttpClientUtil.class);

    /**
     * Performs a simple HTTP GET request.
     */
    public static String get(String url) throws IOException {
        logger.info("HTTP GET: " + url);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet request = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(request);
            try {
                String body = EntityUtils.toString(response.getEntity());
                logger.info("Response status: " + response.getStatusLine().getStatusCode());
                return body;
            } finally {
                response.close();
            }
        } finally {
            httpClient.close();
        }
    }
}
