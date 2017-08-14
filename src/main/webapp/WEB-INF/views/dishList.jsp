<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${typeOfDishes}</title>

    <link href="/resources/css/styles.css" type="text/css" rel="stylesheet"/>

</head>
<body>

<jsp:include page="header.jsp"/>
<jsp:include page="menu.jsp"/>

<fmt:setLocale value="uk_UA" scope="session"/>

<c:forEach items="${dishList}" var="dish">
    <div class="dish-preview-container">
        <ul>
            <li><img class="dish-image" src="/dishImage?id=${dish.id}"/></li>
            <li type="hidden" value="${dish.id}">
            <li>Блюдо: ${dish.name}</li>
            <li>Описание: ${dish.description}</li>
            <li>Цена: <fmt:formatNumber value="${dish.price}" type="currency"/></li>
            <li><a href="/orderDish?id=${dish.id}">В корзину</a></li>
            <security:authorize access="hasRole('ROLE_ADMIN')">
                <li><a href="/editDish?id=${dish.id}">Редактировать блюдо</a></li>
                <li><a href="/delete?id=${dish.id}">Удалить блюдо</a></li>
            </security:authorize>
        </ul>
    </div>
</c:forEach>
</body>
</html>