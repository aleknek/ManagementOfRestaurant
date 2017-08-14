<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<link href="/resources/css/styles.css" type="text/css" rel="stylesheet"/>

<div class="header-container">

    <div class="site-name">Ресторан "Студент"</div>

    <div class="header-bar">

        <c:if test="${pageContext.request.userPrincipal.name != null}">
            Hello
            <a href="/signup?username=${pageContext.request.userPrincipal.name}">
                    ${pageContext.request.userPrincipal.name} </a>
            &nbsp;|&nbsp;
            <a href="/logout">Logout</a>
        </c:if>

        <c:if test="${pageContext.request.userPrincipal.name == null}">
            <a href="/signup">Sign up</a>
            &nbsp;|&nbsp;
            <a href="/login">Login</a>
        </c:if>

    </div>
</div>