<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Присоединиться к групповому заказу</title>

    <link href="/resources/css/styles.css" type="text/css" rel="stylesheet"/>

</head>
<body>

<jsp:include page="header.jsp"/>
<jsp:include page="menu.jsp"/>

<style type="text/css">
    span.error {
        color: red;
    }
</style>

<form:form modelAttribute="groupOrderForm" method="POST" enctype="multipart/form-data">

    <table style="text-align:left;">

        <tr>
            <td>Укажите номер заказа:</td>
            <td><form:input path="numberOfOrder"/></td>
            <td><span class="error"><form:errors path="numberOfOrder"/></span></td>
        </tr>

        <tr>
            <td>&nbsp;</td>
            <td><input type="submit" value="Приступить к заказу"/></td>
        </tr>
    </table>
</form:form>
</body>
</html>