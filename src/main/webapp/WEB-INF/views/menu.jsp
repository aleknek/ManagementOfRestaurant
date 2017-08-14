<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<script type="text/javascript">
    $(document).ready(function () {
        $('#nav li').hover(
                function () {
                    $('ul', this).slideDown(100);
                },
                function () {
                    $('ul', this).slideUp(100);
                }
        );
    });
</script>

<ul id="nav">
    <li><a href="#" class="selected">Меню</a>
        <ul>
            <li><a href="/firstDishes">Первые блюда</a></li>
            <li><a href="/secondDishes">Вторые блюда</a></li>
            <li><a href="/salads">Салаты</a></li>
            <li><a href="/deserts">Десерты</a></li>
            <li><a href="/drinks">Напитки</a></li>
            <security:authorize access="hasRole('ROLE_ADMIN')">
                <li><a href="/editDish">Добавить новое блюдо</a></li>
            </security:authorize>
        </ul>
    </li>
    <li><a href="/basket">Корзина</a></li>

    <li><a>Заказы</a>
        <ul>
            <security:authorize access="hasRole('ROLE_ADMIN')">
                <li><a href="/orderList">Список всех заказов</a></li>
            </security:authorize>
            <li><a href="/myOrderList">Список моих заказов</a></li>
            <li><a href="/createGroupOrder">Создать групповой заказ</a></li>
            <li><a href="/joinToGroupOrder">Присоединиться к груп. заказу</a></li>
        </ul>
    </li>
    <li><a>Группы пользователей</a>
        <ul>
            <security:authorize access="hasRole('ROLE_ADMIN')">
                <li><a href="/userGroupsAll">Все группы пользователей</a></li>
            </security:authorize>
            <li><a href="/userGroups">Мои группы</a></li>
            <li><a href="/joinToGroupUsers">Присоединиться к группе</a></li>
            <li><a href="/getNewUsersOfMyGroup">Новые пользователи</a></li>
            <li><a href="/createNewGroup">Создать новую группу</a></li>
        </ul>
    </li>
</ul>