<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>File Upload</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; background-color: #f5f5f5; }
        .container { max-width: 600px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }
        h1 { color: #333; }
        a { color: #007bff; }
    </style>
</head>
<body>
    <div class="container">
        <h1>&#128206; File Upload</h1>
        <p>Upload a file using Commons FileUpload 1.3.1 (CVE-2016-1000031)</p>

        <form action="upload" method="post" enctype="multipart/form-data">
            <input type="file" name="file"><br><br>
            <button type="submit">Upload</button>
        </form>

        <p><a href="./">&#8592; Back to Top</a></p>
    </div>
</body>
</html>
