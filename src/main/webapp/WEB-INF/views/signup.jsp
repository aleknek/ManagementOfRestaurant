<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Добавление/Редактирование пользователя</title>
    <link href="/resources/css/styles.css" type="text/css" rel="stylesheet"/>
</head>
<body>

<jsp:include page="header.jsp"/>

<style type="text/css">
    span.error {
        color: red;
    }
</style>

<form:form modelAttribute="userForm" method="POST" enctype="multipart/form-data">

    <table style="text-align:left;">

        <tr>
            <td>
                <form:hidden path="id"/>
            </td>
        </tr>

        <tr>
            <td>Login:</td>
            <td><form:input path="username"/></td>
            <td><span class="error"><form:errors path="username"/></span></td>
        </tr>

        <tr>
            <td>Пароль:</td>
            <td><form:password path="password"/></td>
            <td><span class="error"><form:errors path="password"/></span></td>
        </tr>

        <tr>
            <td>Имя:</td>
            <td><form:input path="firstName"/></td>
            <td><span class="error"><form:errors path="firstName"/></span></td>
        </tr>

        <tr>
            <td>Фамилия</td>
            <td><form:input path="lastName"/></td>
            <td><span class="error"><form:errors path="lastName"/></span></td>
        </tr>

        <tr>
            <td>Email:</td>
            <td><form:input path="email"/></td>
            <td><span class="error"><form:errors path="email"/></span></td>
        </tr>

        <tr>
            <td>Телефон</td>
            <td><form:input path="phone"/></td>
            <td><span class="error"><form:errors path="phone"/></span></td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td><input type="submit" value="Сохранить"/></td>
        </tr>
    </table>
</form:form>
</body>
</html>