package com.example.hello.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * File upload servlet using built-in Jakarta Servlet 6.0 multipart support (replaces vulnerable Commons FileUpload 1.3.1).
 * CVE-2016-1000031 no longer applicable as we're using built-in Jakarta Servlet API.
 */
@WebServlet(urlPatterns = {"/upload"})
@MultipartConfig(maxFileSize = 1024 * 1024 * 10) // 10MB max
public class FileUploadServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(FileUploadServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/upload.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("File upload request received");

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            Collection<Part> parts = request.getParts();

            out.println("<html><body>");
            out.println("<h2>Upload Result</h2>");

            for (Part part : parts) {
                if (part.getSubmittedFileName() != null && !part.getSubmittedFileName().isEmpty()) {
                    logger.info("Uploaded file: " + part.getSubmittedFileName() + " (" + part.getSize() + " bytes)");
                    out.println("<p>File: " + part.getSubmittedFileName() + " (" + part.getSize() + " bytes)</p>");
                }
            }

            out.println("<p><a href='upload'>Back</a></p>");
            out.println("</body></html>");

        } catch (Exception e) {
            logger.error("File upload failed", e);
            out.println("<html><body><p>Upload failed: " + e.getMessage() + "</p></body></html>");
        }
    }
}
