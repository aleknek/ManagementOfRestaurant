<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">

    <title>Home</title>

    <link href="/resources/css/styles.css" type="text/css" rel="stylesheet"/>

</head>
<body>

<jsp:include page="header.jsp"/>
<jsp:include page="menu.jsp"/>

<div class="home-container">
    <ul>
        <h2>Привет ${pageContext.request.userPrincipal.name}. <a href="/firstDishes">Приступить к заказу</a></h2>
    </ul>
</div>

</body>
</html>