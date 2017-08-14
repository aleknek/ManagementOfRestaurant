<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Присоединиться к группе</title>

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

<form:form modelAttribute="userGroupForm" method="POST" enctype="multipart/form-data">

    <table style="text-align:left;">

        <tr>
            <td>
                <form:hidden path="id"/>
            </td>
        </tr>

        <tr>
            <td>Номер группы:</td>
            <td><form:input path="numberOfGroup"/></td>
            <td><span class="error"><form:errors path="numberOfGroup"/></span></td>
        </tr>

        <tr>
            <td>&nbsp;</td>
            <td><input type="submit" value="Отправить запрос на вступление в группу"/></td>
        </tr>
    </table>
</form:form>
</body>
</html>