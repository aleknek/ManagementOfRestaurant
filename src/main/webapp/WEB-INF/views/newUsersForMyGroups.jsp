<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Новые пользователи</title>
    <link href="/resources/css/styles.css" type="text/css" rel="stylesheet"/>
</head>
<body>

<jsp:include page="header.jsp"/>
<jsp:include page="menu.jsp"/>

<form:form modelAttribute="userGroupForm" method="POST" enctype="multipart/form-data">
    <c:forEach items="${newUsersForMyGroups}" var="group">
        <c:if test="${not empty group.users}">
            &nbsp;
            <div class="user-info-container">
                <ul>
                    <li>Номер группы: ${group.numberOfGroup}</li>
                </ul>
                <table border="1" style="width:100%">
                    <tr>
                        <th>Имя</th>
                        <th>Фамилия</th>
                        <th>E-mail</th>
                        <th>Телефон</th>
                        <th></th>
                        <th></th>
                    </tr>
                    <c:forEach items="${group.users}" var="user">
                        <tr>
                            <td>${user.getFirstName()}</td>
                            <td>${user.getLastName()}</td>
                            <td>${user.getEmail()}</td>
                            <td>${user.getPhone()}</td>
                            <td>
                                <a href="/acceptNewUserOfGroup?idUser=${user.id}&numberOfGroup=${group.numberOfGroup}">Принять
                                    пользователя в группу</a>
                            <td>
                                <a href="/rejectNewUserOfGroup?idUser=${user.id}&numberOfGroup=${group.numberOfGroup}">Отклонить</a>
                        </tr>
                    </c:forEach>
                </table>
                &nbsp;
            </div>
        </c:if>
    </c:forEach>
</form:form>
</body>
</html>