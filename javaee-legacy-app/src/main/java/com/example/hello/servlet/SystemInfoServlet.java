package com.example.hello.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.example.hello.util.DateUtil;
import com.example.hello.util.EncodingUtil;
import com.example.hello.util.ScriptUtil;
import com.example.hello.util.SecurityUtil;
import com.example.hello.util.XmlUtil;
import com.example.hello.util.XmlUtil.UserXml;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * System info servlet that exercises various deprecated/removed Java APIs.
 * Useful for demonstrating migration issues from Java 8 to Java 25.
 */
@WebServlet(urlPatterns = {"/sysinfo"})
public class SystemInfoServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(SystemInfoServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        logger.info("SystemInfoServlet accessed");

        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
        out.println("<title>System Info</title>");
        out.println("<style>body{font-family:Arial,sans-serif;margin:40px;background:#f5f5f5}");
        out.println(".container{max-width:800px;margin:0 auto;background:white;padding:30px;border-radius:8px;box-shadow:0 2px 4px rgba(0,0,0,0.1)}");
        out.println("h1{color:#333}h2{color:#555;border-bottom:1px solid #eee;padding-bottom:8px}");
        out.println("pre{background:#f8f8f8;padding:10px;border-radius:4px;overflow-x:auto}");
        out.println(".ok{color:green}.warn{color:orange}.err{color:red}");
        out.println("a{color:#007bff}</style></head><body><div class='container'>");

        out.println("<h1>&#128736; System Info & Legacy API Demo</h1>");

        // --- sun.misc.BASE64Encoder (removed in Java 11) ---
        out.println("<h2>Base64 Encoding (sun.misc.BASE64Encoder)</h2>");
        try {
            String encoded = EncodingUtil.encodeString("Hello, Java 8!");
            String decoded = EncodingUtil.decodeString(encoded);
            out.println("<pre>Original: Hello, Java 8!\nEncoded:  " + encoded + "\nDecoded:  " + decoded + "</pre>");
            out.println("<p class='warn'>&#9888; Uses sun.misc.BASE64Encoder/Decoder (removed in Java 11)</p>");
        } catch (Throwable e) {
            out.println("<p class='err'>&#10060; " + e.getClass().getName() + ": " + e.getMessage() + "</p>");
            out.println("<p class='warn'>&#9888; sun.misc.BASE64Encoder/Decoder is not accessible in this environment</p>");
        }

        // --- JAXB (removed in Java 11) ---
        out.println("<h2>XML Serialization (javax.xml.bind / JAXB)</h2>");
        try {
            UserXml user = new UserXml("Taro", "taro@example.com", 30);
            String xml = XmlUtil.toXml(user);
            out.println("<pre>" + escapeHtml(xml) + "</pre>");

            String hex = XmlUtil.bytesToHex("Hello".getBytes());
            out.println("<pre>Hex: " + hex + "</pre>");
            out.println("<p class='warn'>&#9888; Uses javax.xml.bind (JAXB) - removed in Java 11 (JEP 320)</p>");
        } catch (Throwable e) {
            out.println("<p class='err'>&#10060; " + e.getClass().getName() + ": " + e.getMessage() + "</p>");
        }

        // --- Nashorn (removed in Java 15) ---
        out.println("<h2>JavaScript Evaluation (Nashorn)</h2>");
        try {
            Object result = ScriptUtil.evaluateJs("1 + 2 * 3");
            out.println("<pre>Expression: 1 + 2 * 3 = " + result + "</pre>");
            out.println("<p class='warn'>&#9888; Uses Nashorn JavaScript engine (removed in Java 15, JEP 372)</p>");
        } catch (Throwable e) {
            out.println("<p class='err'>&#10060; Nashorn not available: " + e.getMessage() + "</p>");
        }

        // --- Deprecated wrapper constructors (removed in Java 16) ---
        out.println("<h2>Deprecated Wrapper Constructors</h2>");
        try {
            Integer days = DateUtil.daysBetween(new Date(0), new Date());
            Double hours = DateUtil.millisToHours(System.currentTimeMillis());
            Boolean valid = DateUtil.isValidDate("2025-01-01");
            out.println("<pre>Days since epoch: " + days + "\nHours since epoch: "
                    + "%.1f".formatted(hours) + "\nIs '2025-01-01' valid: " + valid + "</pre>");
            out.println("<p class='warn'>&#9888; Uses new Integer(), new Double(), new Boolean() (removed in Java 16+)</p>");
        } catch (Throwable e) {
            out.println("<p class='err'>&#10060; " + e.getClass().getName() + ": " + e.getMessage() + "</p>");
        }

        // --- SecurityManager (removed in Java 24) ---
        out.println("<h2>SecurityManager (Removed)</h2>");
        try {
            boolean canRead = SecurityUtil.canReadFile("/etc/passwd");
            out.println("<pre>SecurityManager API has been removed in Java 24 (JEP 486)\nFile read check (using Files.isReadable): " + canRead + "</pre>");
            out.println("<p class='info'>&#9989; SecurityManager APIs removed and replaced with modern alternatives</p>");
        } catch (Throwable e) {
            out.println("<p class='err'>&#10060; " + e.getClass().getName() + ": " + e.getMessage() + "</p>");
        }

        // --- Date/Time info ---
        out.println("<h2>Date Utilities (java.util.Date + SimpleDateFormat)</h2>");
        out.println("<pre>Current date: " + DateUtil.getCurrentDate()
                + "\nCurrent datetime: " + DateUtil.formatDateTime(new Date()) + "</pre>");
        out.println("<p class='warn'>&#9888; Uses java.util.Date + SimpleDateFormat (thread-unsafe, migrate to java.time)</p>");

        // --- Java version info ---
        out.println("<h2>Runtime Info</h2>");
        out.println("<pre>Java version: " + System.getProperty("java.version")
                + "\nJava vendor:  " + System.getProperty("java.vendor")
                + "\nOS:           " + System.getProperty("os.name") + " " + System.getProperty("os.version")
                + "\nEncoding:     " + System.getProperty("file.encoding") + "</pre>");

        out.println("<p><a href='./'>&larr; Back to Top</a></p>");
        out.println("</div></body></html>");
    }

    private String escapeHtml(String text) {
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
