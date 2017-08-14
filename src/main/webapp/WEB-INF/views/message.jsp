<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link href="/resources/css/styles.css" type="text/css" rel="stylesheet"/>
</head>
<body>

<jsp:include page="header.jsp"/>
<jsp:include page="menu.jsp"/>

<div class="container">
    &nbsp;&nbsp;
    <h2>${message}</h2>
</div>

</body>
</html>