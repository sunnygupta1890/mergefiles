<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
${filename}
<h1>Upload Success</h1>
<center>
        <h2><a href='/download?filename=${filename}' > Click here to download file ${filename} </a></h2>
</center>
</body>
</html>