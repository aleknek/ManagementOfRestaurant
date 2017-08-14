<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Ресторан "Студент"</title>
    <link href="/resources/css/styles.css" type="text/css" rel="stylesheet"/>
</head>
<body>

<jsp:include page="header.jsp"/>

<div class="index-container">
    <h3>Добро пожаловать на сайт ресторана "Студент". <a href="/login">Авторизуйтесть</a> или <a
            href="/signup?username=${pageContext.request.userPrincipal.name}">зарегистрируйтесь</a>, чтобы заказать
        чего-нибудь
        вкусненького.</h3>
</div>
</body>
</html>