<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Hello</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; background-color: #f5f5f5; }
        .container { max-width: 600px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        h1 { color: #2c7a2c; }
        .info { background-color: #e8f4f8; padding: 15px; border-radius: 4px; margin: 20px 0; }
        a { color: #007bff; }
    </style>
</head>
<body>
    <div class="container">
        <h1>${greeting}</h1>

        <div class="info">
            <strong>Java Version:</strong> ${javaVersion}<br>
            <strong>Server Info:</strong> ${serverInfo}
        </div>

        <form action="hello" method="get">
            <label for="name">Enter your name:</label>
            <input type="text" id="name" name="name" placeholder="World">
            <button type="submit">Say Hello</button>
        </form>

        <p><a href="./">&#8592; Back to Top</a></p>
    </div>
</body>
</html>
