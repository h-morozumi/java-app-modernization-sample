package com.example.hello.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * REST-like API servlet that returns user list as JSON.
 * Uses old javax.servlet and vulnerable Jackson Databind.
 */
@WebServlet(urlPatterns = {"/api/users"})
public class UserApiServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(UserApiServlet.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("UserApiServlet accessed");

        List<Map<String, String>> users = new ArrayList<>();

        Map<String, String> user1 = new HashMap<>();
        user1.put("id", "1");
        user1.put("name", "Taro Yamada");
        user1.put("email", "taro@example.com");
        users.add(user1);

        Map<String, String> user2 = new HashMap<>();
        user2.put("id", "2");
        user2.put("name", "Hanako Suzuki");
        user2.put("email", "hanako@example.com");
        users.add(user2);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print(objectMapper.writeValueAsString(users));
        out.flush();
    }
}
