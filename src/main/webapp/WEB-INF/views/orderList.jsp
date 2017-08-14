<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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

<fmt:setLocale value="uk_UA" scope="session"/>

<table border="1" style="width:100%">
    <tr>
        <th>№ заказа</th>
        <th>Дата заказа</th>
        <th>Сумма</th>
        <th>Статус заказа</th>
        <th>Детально...</th>
    </tr>
    <c:forEach items="${orderList}" var="order" varStatus="orderInfo">
        <tr>
            <td>${order.numberOfOrder}</td>
            <td>
                <fmt:formatDate value="${order.dateOfOrder}" pattern="dd-MM-yyyy HH:mm"/>
            </td>
            <td style="color:red;">
                <fmt:formatNumber value="${order.sum}" type="currency"/>
            </td>
            <td>${order.getOrderStatus().getName()}</td>
            <td><a href="/order?numberOfOrder=${order.numberOfOrder}">Просмотр</a>
                <c:if test="${order.getOrderStatus().getName() == 'Новый'}">
                <a href="/editOrder?numberOfOrder=${order.numberOfOrder}">Редактировать</a>
            </td>
            </c:if>
        </tr>
    </c:forEach>
</table>

</body>
</html>