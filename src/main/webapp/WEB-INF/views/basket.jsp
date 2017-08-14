<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Корзина</title>
    <link href="/resources/css/styles.css" type="text/css" rel="stylesheet"/>

    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <script src="/resources/js/jquery.formatCurrency-1.4.0.min.js" type="text/javascript"></script>

    <script type="text/javascript">

        function doAjax(arg, currentSumOfUser) {

            var idDish = $('#idDish' + arg).val();
            var quantity = $('#quantity' + arg).val();
            var currentSumOfDish = $('#currentSumOfDish' + arg).val();
            var sumTotal = $('#sumTotal').val();
            var idUser = $('#idUser' + arg).val();

            $.ajax({
                type: "POST",
                url: 'doAjax',
                data: "idDish=" + idDish + "&quantity=" + quantity + "&currentSumOfDish=" + currentSumOfDish + "&sumTotal=" + sumTotal + "&currentSumOfUser=" + currentSumOfUser,
                dataType: "json",
                success: function (response) {
                    $('#sum' + arg).html(response.sum);
                    $('#total').html(response.sumTotal);
                    $('#newSumOfUser' + idUser).html(response.sumTotalOfUser);
                },
                error: function () {
                    alert('Некорректное значение');
                }
            });
        }
    </script>
</head>

<body>

<jsp:include page="header.jsp"/>
<jsp:include page="menu.jsp"/>

<fmt:setLocale value="uk_UA" scope="session"/>

<form:form method="POST" modelAttribute="orderForm"
           action="/basket">

    <!--suppress HtmlUnknownTarget -->
    <a class="navi-item" type="submit" href="/firstDishes">Продолжить покупки</a>
    <input type="submit" name="statusNew" value="Сохранить заказ (доступен для редактирования)"
           class="buttonStyle"/>
    <input type="submit" name="statusSent" value="Отправить заказ в работу" class="buttonStyle"/>
    <input type="submit" name="statusCancel" value="Завершить без сохранения" class="buttonStyle"/>
    &nbsp;

    <c:choose>
        <c:when test="${orderForm.numberOfOrder == 0}">
            <h3>Заказ № : Новый</h3>
        </c:when>
        <c:otherwise>
            <h3>Заказ № : ${orderForm.numberOfOrder}</h3>
        </c:otherwise>
    </c:choose>

    <h3>Сумма заказа общая: <span id="total" class="total">
        <fmt:formatNumber value="${sumTotal}" type="currency"/></span>
    </h3>

    <form:hidden id="sumTotal" path="sum"/>

    &nbsp;
    <c:forEach items="${userList}" var="user" varStatus="countUser">

        &nbsp;
        <div class="user-info-container">

            <c:set var="currentSumOfUser" value="${0}"/>

            <ul>
                <li><h3>Сумма заказа по пользователю - ${user.lastName} ${user.firstName}:
                    <span id="newSumOfUser${user.id}" class="total"><fmt:formatNumber value="${orderForm.getSum(user)}"
                                                                                      type="currency"/></span>
                </h3></li>
            </ul>

            <div id="orderItemsContainer" class="container">
                <c:forEach items="${orderForm.orderItems}" var="item" varStatus="count">

                    <c:if test="${item.user.id == user.id}">
                        <div class="dish-preview-container">

                            <ul>
                                <li><form:hidden id="idDish${count.index}" path="orderItems[${count.index}].dish.id"
                                                 class="idDish"/></li>
                                <li><form:hidden id="idUser${count.index}"
                                                 path="orderItems[${count.index}].user.id"/></li>
                                <li><form:hidden id="currentSumOfDish${count.index}"
                                                 path="orderItems[${count.index}].sum"/></li>
                                <li>
                                    <img class="dish-image" src="/dishImage?id=${item.dish.id}"/>
                                </li>
                                <li>Блюдо: ${item.dish.name}</li>
                                <li>
                                    Цена:  <span id="price" name="price" class="price">
                                    <fmt:formatNumber value="${item.dish.price}" type="currency"/></span>
                                </li>

                                <c:choose>
                                    <c:when test="${pageContext.request.userPrincipal.name == user.getUsername()}">

                                        <c:set var="currentSumOfUser" value="${currentSumOfUser + item.sum}"/>

                                        <li>Кол-во:
                                            <form:input path="orderItems[${count.index}].quantity"
                                                        id="quantity${count.index}"
                                                        onkeyup="doAjax(${count.index},${currentSumOfUser})"/>
                                        </li>
                                        <li>Сумма:<span id="sum${count.index}" class="subtotal">
                                            <fmt:formatNumber value="${item.sum}" type="currency"/></span></li>
                                        <li>
                                            <a href="/removeDish?id=${item.dish.id}">Удалить</a>
                                        </li>
                                    </c:when>
                                    <c:otherwise>
                                        <li>Кол-во: ${item.quantity}</li>
                                        <li>
                                            Сумма:<span class="subtotal">
                                            <fmt:formatNumber value="${item.sum}" type="currency"/></span>
                                        </li>
                                    </c:otherwise>
                                </c:choose>
                            </ul>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </div>
    </c:forEach>
</form:form>
</body>
</html>