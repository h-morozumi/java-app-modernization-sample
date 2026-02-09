<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Java Hello - Legacy JavaEE App</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; background-color: #f5f5f5; }
        .container { max-width: 800px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        h1 { color: #333; }
        .info { background-color: #e8f4f8; padding: 15px; border-radius: 4px; margin: 20px 0; }
        .warn { background-color: #fff3cd; padding: 15px; border-radius: 4px; margin: 20px 0; }
        a { color: #007bff; text-decoration: none; }
        a:hover { text-decoration: underline; }
        ul { line-height: 2; }
    </style>
</head>
<body>
    <div class="container">
        <h1>&#9749; Java Hello - Legacy JavaEE Application</h1>

        <div class="warn">
            <strong>&#9888; This application uses outdated dependencies with known CVEs.</strong><br>
            It is intended as a migration hands-on target for GitHub Copilot Migration.
        </div>

        <h2>Pages</h2>
        <ul>
            <li><a href="hello">Hello Servlet</a> - Greeting page (try <a href="hello?name=Copilot">hello?name=Copilot</a>)</li>
            <li><a href="api/users">User API</a> - JSON API endpoint (Jackson Databind)</li>
            <li><a href="upload">File Upload</a> - File upload page (Commons FileUpload)</li>
            <li><a href="sysinfo">System Info</a> - Legacy API demo (Base64, JAXB, Nashorn, SecurityManager, etc.)</li>
        </ul>

        <div class="info">
            <strong>Tech Stack:</strong><br>
            Java EE 7 / Servlet 3.1 / JSP 2.3 / JSTL 1.2<br>
            Log4j 1.2.17 / Jackson 2.9.8 / Commons Collections 3.2.1<br>
            Commons FileUpload 1.3.1 / HttpClient 4.5.2
        </div>

        <h2>Known CVEs in Dependencies</h2>
        <table border="1" cellpadding="8" cellspacing="0" style="border-collapse: collapse; width: 100%;">
            <tr style="background-color: #f8d7da;">
                <th>Library</th><th>Version</th><th>CVE</th>
            </tr>
            <tr><td>Log4j</td><td>1.2.17</td><td>CVE-2021-4104</td></tr>
            <tr><td>Commons Collections</td><td>3.2.1</td><td>CVE-2015-6420</td></tr>
            <tr><td>Commons FileUpload</td><td>1.3.1</td><td>CVE-2016-1000031</td></tr>
            <tr><td>Commons IO</td><td>2.4</td><td>CVE-2021-29425</td></tr>
            <tr><td>Jackson Databind</td><td>2.9.8</td><td>CVE-2019-12384 etc.</td></tr>
            <tr><td>HttpClient</td><td>4.5.2</td><td>CVE-2020-13956</td></tr>
        </table>

        <h2>Deprecated/Removed Java APIs (Java 8 &rarr; 25)</h2>
        <table border="1" cellpadding="8" cellspacing="0" style="border-collapse: collapse; width: 100%;">
            <tr style="background-color: #fff3cd;">
                <th>API</th><th>Status</th><th>Replacement</th>
            </tr>
            <tr><td>sun.misc.BASE64Encoder/Decoder</td><td>Removed in Java 11</td><td>java.util.Base64</td></tr>
            <tr><td>javax.xml.bind (JAXB)</td><td>Removed in Java 11 (JEP 320)</td><td>jakarta.xml.bind or Jackson XML</td></tr>
            <tr><td>Nashorn (javax.script)</td><td>Removed in Java 15 (JEP 372)</td><td>GraalVM JS / external engine</td></tr>
            <tr><td>new Integer/Long/Boolean/Double()</td><td>Removed in Java 16</td><td>valueOf() factory methods</td></tr>
            <tr><td>Thread.stop()</td><td>Removed in Java 20 (JEP 449)</td><td>Cooperative interruption</td></tr>
            <tr><td>finalize()</td><td>Deprecated for removal (JEP 421)</td><td>AutoCloseable / Cleaner</td></tr>
            <tr><td>SecurityManager</td><td>Removed in Java 24 (JEP 486)</td><td>Other security mechanisms</td></tr>
            <tr><td>javax.servlet / javax.* EE</td><td>Migrated</td><td>jakarta.servlet / jakarta.*</td></tr>
        </table>
    </div>
</body>
</html>
