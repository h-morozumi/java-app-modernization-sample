package com.example.hello.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

/**
 * File upload servlet using vulnerable Commons FileUpload 1.3.1.
 * CVE-2016-1000031: DiskFileItem class can be exploited for RCE.
 */
@WebServlet(urlPatterns = {"/upload"})
public class FileUploadServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(FileUploadServlet.class);

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

        if (!ServletFileUpload.isMultipartContent(request)) {
            out.println("<html><body><p>No file uploaded.</p></body></html>");
            return;
        }

        try {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setSizeMax(1024 * 1024 * 10); // 10MB max

            List<FileItem> items = upload.parseRequest(request);

            out.println("<html><body>");
            out.println("<h2>Upload Result</h2>");

            for (FileItem item : items) {
                if (!item.isFormField()) {
                    logger.info("Uploaded file: " + item.getName() + " (" + item.getSize() + " bytes)");
                    out.println("<p>File: " + item.getName() + " (" + item.getSize() + " bytes)</p>");
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
