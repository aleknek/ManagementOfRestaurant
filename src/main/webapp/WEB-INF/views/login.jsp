<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">

    <title>Login</title>

    <link href="/resources/css/styles.css" type="text/css" rel="stylesheet"/>

</head>
<body>

<jsp:include page="header.jsp"/>

<div class="login-container">

    <ul>
        <li>Пользователь: admin, пароль: admin</li>
        <li>Пользователь: user1, пароль: user1</li>
        <li>Пользователь: user2, пароль: user2</li>
        <li>Пользователь: user3, пароль: user3</li>
    </ul>

    <h3>Введите логин и пароль</h3>

    <br>
    <c:if test="${param.error == 'true'}">
        <div style="color: red; margin: 10px 0px;">
            Login Failed!!!<br/> Reason :
                ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}

        </div>
    </c:if>
    <form method="POST"
          action="${pageContext.request.contextPath}/j_spring_security_check">
        <table>
            <tr>
                <td>Логин</td>
                <td><input name="userName"/></td>
            </tr>
            <tr>
                <td>Пароль</td>
                <td><input type="password" name="password"/></td>
            </tr>
            <tr>
                <td>&nbsp;</td>
                <td><input type="submit" value="Login"/></td>
            </tr>
        </table>
    </form>
    <span class="error-message">${error}</span>
</div>
</body>
</html>