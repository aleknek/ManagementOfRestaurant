<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Список блюд</title>

    <link href="/resources/css/styles.css" type="text/css" rel="stylesheet"/>

</head>
<body>

<jsp:include page="header.jsp"/>
<jsp:include page="menu.jsp"/>

<fmt:setLocale value="uk_UA" scope="session"/>

&nbsp;
<h3>Заказ № : ${order.numberOfOrder}</h3>
<h3>Сумма заказа общая: <span class="total"><fmt:formatNumber value="${sumTotal}" type="currency"/></span></h3>
&nbsp;

<c:forEach items="${userList}" var="user">

    <div class="user-info-container">

        <h3>Сумма заказа по пользователю - ${user.lastName} ${user.firstName}:
          <span class="total">
            <fmt:formatNumber value="${order.getSum(user)}" type="currency"/>
          </span></h3>
        &nbsp;

        <table border="1" style="width:100%">
            <tr>
                <th>Наименование блюда</th>
                <th>Количество</th>
                <th>Цена</th>
                <th>Сумма</th>
            </tr>
            <c:forEach items="${order.orderItems}" var="orderDetailInfo">
                <c:if test="${orderDetailInfo.user.id == user.id}">
                    <tr>
                        <td>${orderDetailInfo.dish.getName()}</td>
                        <td>${orderDetailInfo.quantity}</td>
                        <td>
                            <fmt:formatNumber value="${orderDetailInfo.price}" type="currency"/>
                        </td>
                        <td>
                            <fmt:formatNumber value="${orderDetailInfo.sum}" type="currency"/>
                        </td>
                    </tr>
                </c:if>
            </c:forEach>
        </table>
    </div>
    &nbsp;
</c:forEach>
</body>
</html>