<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Список групп пользователя</title>
    <link href="/resources/css/styles.css" type="text/css" rel="stylesheet"/>
</head>
<body>

<jsp:include page="header.jsp"/>
<jsp:include page="menu.jsp"/>

<fmt:setLocale value="uk_UA" scope="session"/>

<c:forEach items="${userGroups}" var="group">
    &nbsp;
    <div class="user-info-container">
        <ul>
            <li>Номер группы: ${group.numberOfGroup}</li>
            <li>Администратор: ${group.user.getFirstName()} ${group.user.getLastName()}</li>
            <li><a href="/quitGroup?numberOfGroup=${group.numberOfGroup}">Выйти из
                группы</a>
                <c:if test="${group.user.username == pageContext.request.userPrincipal.name}"></li>
            <li><a href="/deleteGroup?numberOfGroup=${group.numberOfGroup}">Удалить
                группу</a>
                </c:if></li>
        </ul>

        <table border="1" style="width:100%">
            <tr>
                <th>Имя</th>
                <th>Фамилия</th>
                <th>E-mail</th>
                <th>Телефон</th>
            </tr>
            <c:forEach items="${group.users}" var="user">
                <tr>
                    <td>${user.getFirstName()}</td>
                    <td>${user.getLastName()}</td>
                    <td>${user.getEmail()}</td>
                    <td>${user.getPhone()}</td>
                </tr>
            </c:forEach>
        </table>
        &nbsp;
    </div>
</c:forEach>
</body>
</html>