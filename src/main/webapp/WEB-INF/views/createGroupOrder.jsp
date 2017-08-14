<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Создание группового заказа</title>

    <link href="/resources/css/styles.css" type="text/css" rel="stylesheet"/>

</head>
<body>

<jsp:include page="header.jsp"/>
<jsp:include page="menu.jsp"/>

<form:form modelAttribute="groupOrderForm" method="POST" enctype="multipart/form-data">

    <div class="container">
        &nbsp;&nbsp;
        <h2>Вы состоите в нескольких пользовательских группах. Выберите одну из ваших групп, в которой вы хотите
            сделать заказ</h2>
        <select id="currentGroup" name="numberOfGroup">
            <c:forEach var="currentGroup" items="${userGroupList}">
                <option value="${currentGroup.getNumberOfGroup()}">${currentGroup.getNumberOfGroup()}</option>
            </c:forEach>
        </select>
        <input type="submit" value="Создать групповой заказ"/>
    </div>
</form:form>
</body>
</html>