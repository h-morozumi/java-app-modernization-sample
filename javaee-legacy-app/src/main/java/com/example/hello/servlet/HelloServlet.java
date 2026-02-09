package com.example.hello.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * Main servlet - displays the top page.
 * Uses old javax.servlet API and Log4j 1.x.
 */
@WebServlet(urlPatterns = {"/hello"})
public class HelloServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(HelloServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("HelloServlet accessed from: " + request.getRemoteAddr());

        String name = request.getParameter("name");
        if (name == null || name.isEmpty()) {
            name = "World";
        }

        request.setAttribute("greeting", "Hello, " + name + "!");
        request.setAttribute("javaVersion", System.getProperty("java.version"));
        request.setAttribute("serverInfo", getServletContext().getServerInfo());

        request.getRequestDispatcher("/WEB-INF/views/hello.jsp").forward(request, response);
    }
}
