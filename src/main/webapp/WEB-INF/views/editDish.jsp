<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Добавление/Редактирование блюда</title>

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

<form:form modelAttribute="dishForm" method="POST" enctype="multipart/form-data">
    <table style="text-align:left;">
        <tr>
            <td>
                <form:hidden path="id"/>
            </td>
        </tr>

        <tr>
            <td>Наименование:</td>
            <td><form:input path="name"/></td>
            <td><span class="error"><form:errors path="name"/></span></td>
        </tr>

        <tr>
            <td>Описание:</td>
            <td><form:input path="description"/></td>
        </tr>

        <tr>
            <td>Тип блюда:</td>
            <td>
                <form:select path="typeDishes">
                    <form:options items="${typeDishes}" itemLabel="name"/>
                </form:select>
            </td>
            <td><span class="error"><form:errors path="typeDishes"/></span></td>
        </tr>

        <tr>
            <td>Цена:</td>
            <td><form:input path="price"/></td>
            <td><span class="error"><form:errors path="price"/></span></td>
        </tr>
        <tr>
            <td>Изображение:</td>
            <td>
                <img src="/dishImage?id=${dishForm.id}" width="100"/></td>
            <td></td>
        </tr>
        <tr>
            <td>Загрузить изображение:</td>
            <td><form:input type="file" path="fileData"/></td>
            <td></td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td><input type="submit" value="Записать"/>
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>