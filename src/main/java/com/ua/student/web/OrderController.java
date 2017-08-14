package com.ua.student.web;

import com.ua.student.dao.DishDAO;
import com.ua.student.dao.OrderDAO;
import com.ua.student.dao.UserDAO;
import com.ua.student.dao.UserGroupsDAO;
import com.ua.student.model.*;
import com.ua.student.service.OrderService;
import com.ua.student.validator.OrderGroupValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private UserGroupsDAO userGroupsDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private OrderService orderService;

    @Autowired
    private DishDAO dishDAO;

    @Autowired
    private OrderGroupValidator orderGroupValidator;

    @InitBinder("groupOrderForm")
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        if (target.getClass() == OrderGroup.class) {
            dataBinder.setValidator(orderGroupValidator);
        }
    }

    @RequestMapping(value = {"/orderList"}, method = RequestMethod.GET)
    public String orderList(Model model) {
        List<Order> orderList = orderDAO.getListOrders();
        model.addAttribute("title", "Список всех заказов");
        model.addAttribute("orderList", orderList);
        return "orderList";
    }

    @RequestMapping(value = {"/myOrderList"}, method = RequestMethod.GET)
    public String myOrderList(Model model, Authentication authentication) {
        List<Order> orderList = orderDAO.getListMyOrders(userDAO.getUserByUsername(authentication.getName()));
        model.addAttribute("title", "Список моих заказов");
        model.addAttribute("orderList", orderList);
        return "orderList";
    }

    @RequestMapping(value = {"/order"}, method = RequestMethod.GET)
    public String orderView(Model model, @RequestParam("numberOfOrder") String numberOfOrder) {

        Order order = null;
        if (numberOfOrder != null) {
            order = orderDAO.getOrderByNumberOfOrder(Integer.valueOf(numberOfOrder));
        }
        if (order == null) {
            return "redirect:/orderList";
        }

        List<OrderItems> orderListItems = orderDAO.getOrderItemsByNumberOfOrder(order.getNumberOfOrder());
        for (OrderItems item : orderListItems) {
            orderService.addDish(item, order.getOrderItems());
        }

        if (order.getOrderItems().isEmpty()) {
            model.addAttribute("message", "Еще ничего не заказано. Самое время что-то заказать." +
                    " <a href=\"/firstDishes\">Приступить к заказу</a>");
            model.addAttribute("title", "Корзина");
            return "message";
        }

        model.addAttribute("sumTotal", order.getSum());
        model.addAttribute("userList", order.getUserList());
        model.addAttribute("order", order);

        return "order";
    }

    @RequestMapping(value = {"/editOrder"}, method = RequestMethod.GET)
    public String editOrder(HttpServletRequest request,
                            Model model,
                            @RequestParam("numberOfOrder") String numberOfOrder) {

        orderService.removeOrderInSession(request);
        Order order = orderDAO.getOrderByNumberOfOrder(Integer.valueOf(numberOfOrder));

        List<OrderItems> orderItemsList = orderDAO.getOrderItemsByNumberOfOrder(Integer.valueOf(numberOfOrder));

        for (OrderItems item : orderItemsList) {
            orderService.addDish(item, order.getOrderItems());
        }

        request.getSession().setAttribute("myOrder", order);

        if (order.getOrderItems().isEmpty()) {
            model.addAttribute("message", "Еще ничего не заказано. Самое время что-то заказать." +
                    " <a href=\"/firstDishes\">Приступить к заказу</a>");
            model.addAttribute("title", "Корзина");
            return "message";
        }

        model.addAttribute("userList", order.getUserList());
        model.addAttribute("orderForm", order);
        model.addAttribute("sumTotal", order.getSum());

        return "basket";

    }

    @RequestMapping(value = {"/createGroupOrder"}, method = RequestMethod.GET)
    public String createGroupOrder(ModelMap model, Authentication authentication) {

        List<UserGroups> userGroupList = userGroupsDAO.getUserGroupsByIdUser(userDAO.getUserByUsername(authentication.getName()).getId());
        if (userGroupList.isEmpty()) {
            model.addAttribute("message", "Вы не состоите ни в одной группе. Создание группового заказа невозможно." +
                    " Для возможности создания группового заказа, создайте группу пользователей.");
            model.addAttribute("title", "Создание группового заказа");
            return "message";
        }

        if (userGroupList.size() == 1) {
            OrderGroup orderGroup = new OrderGroup();
            int numberOfGroup = userGroupList.get(0).getNumberOfGroup();
            orderDAO.insert(orderGroup, numberOfGroup, OrderStatuses.newOrder);
            model.addAttribute("title", "Создание группового заказа");
            model.addAttribute("message", "Поздравляем. Вы создали групповой заказ № " + numberOfGroup + ". Поделитесь номером заказа с друзьями, чтобы вместе составить заказ");
            return "message";
        }
        model.addAttribute("userGroupList", userGroupList);

        model.addAttribute("orderForm", new Order());
        return "createGroupOrder";
    }

    @RequestMapping(value = {"/createGroupOrder"}, method = RequestMethod.POST)
    public String createGroupOrder(HttpServletRequest request, Model model, @RequestParam("numberOfGroup") String numberOfGroupOrder) {

        OrderGroup orderGroup = new OrderGroup();
        orderDAO.insert(orderGroup, Integer.valueOf(numberOfGroupOrder), OrderStatuses.newOrder);
        orderGroup.setUserGroup(userGroupsDAO.getUserGroupByNumberOfGroup(Integer.valueOf(numberOfGroupOrder)));
        orderService.setOrderInSession(request, orderGroup);
        model.addAttribute("title", "Создание группового заказа");
        model.addAttribute("message", "Поздравляем. Вы создали групповой заказ № " + orderGroup.getNumberOfOrder() + ". Поделитесь номером заказа с друзьями, чтобы вместе составить заказ");
        return "message";
    }

    @RequestMapping(value = {"/joinToGroupOrder"}, method = RequestMethod.GET)
    public String joinToGroupOrder(ModelMap model) {

        OrderGroup orderGroup = new OrderGroup();
        model.put("groupOrderForm", orderGroup);
        return "joinToGroupOrder";

    }

    @RequestMapping(value = {"/joinToGroupOrder"}, method = RequestMethod.POST)
    public String joinToGroupOrder(HttpServletRequest request,
                                   Model model,
                                   @ModelAttribute("groupOrderForm") @Validated OrderGroup orderGroup,
                                   BindingResult result,
                                   @RequestParam("numberOfOrder") String numberOfOrder) {


        if (result.hasErrors()) {
            return "joinToGroupOrder";
        }

        Order order = orderDAO.getOrderByNumberOfOrder(Integer.valueOf(numberOfOrder));

        List<OrderItems> orderItemsList = orderDAO.getOrderItemsByNumberOfOrder(Integer.valueOf(numberOfOrder));

        for (OrderItems item : orderItemsList) {
            orderService.addDish(item, order.getOrderItems());
        }
        request.getSession().setAttribute("myOrder", order);

        model.addAttribute("userList", order.getUserList());
        model.addAttribute("orderForm", order);
        model.addAttribute("sumTotal", order.getSum());

        return "basket";

    }

    @RequestMapping(value = {"/basket"}, method = RequestMethod.GET)
    public String basket(HttpServletRequest request, Model model) {

        Order order = orderService.getOrderInSession(request);
        order.setSum(orderService.getSumTotal(order.getOrderItems()));

        model.addAttribute("orderForm", order);
        model.addAttribute("userList", order.getUserList());
        model.addAttribute("sumTotal", order.getSum());

        if (order.getOrderItems().isEmpty()) {
            model.addAttribute("message", "Еще ничего не заказано. Самое время что-то заказать." +
                    " <a href=\"/firstDishes\">Приступить к заказу</a>");
            model.addAttribute("title", "Корзина");
            return "message";
        }

        return "basket";
    }

    @RequestMapping(value = {"/basket"}, params = "statusSent", method = RequestMethod.POST)
    public String saveOrderStatusSent(HttpServletRequest request,
                                      Model model,
                                      @ModelAttribute("orderForm") Order orderForm,
                                      @RequestParam(value = "idUserGroup", defaultValue = "") String idUserGroup,
                                      Authentication authentication) {
        return orderService.saveOrder(orderForm, request, authentication, model, OrderStatuses.closed);
    }

    @RequestMapping(value = {"/basket"}, params = "statusNew", method = RequestMethod.POST)
    public String saveOrderStatusNew(HttpServletRequest request,
                                     Model model,
                                     @ModelAttribute("orderForm") Order orderForm,
                                     @RequestParam(value = "idUserGroup", defaultValue = "") String idUserGroup,
                                     Authentication authentication) {

        return orderService.saveOrder(orderForm, request, authentication, model, OrderStatuses.newOrder);
    }

    @RequestMapping(value = {"/basket"}, params = "statusCancel", method = RequestMethod.POST)
    public String saveOrderStatusCancel(HttpServletRequest request) {
        orderService.removeOrderInSession(request);
        return "redirect:/firstDishes";
    }

    @RequestMapping(value = "/doAjax", method = RequestMethod.POST)
    public
    @ResponseBody
    OrderItemsJSON checkStrength(@RequestParam String idDish,
                                 @RequestParam String quantity,
                                 @RequestParam String currentSumOfDish,
                                 @RequestParam String sumTotal,
                                 @RequestParam String currentSumOfUser
    ) {

        Dish dish = dishDAO.getDishById(Integer.valueOf(idDish));
        double newSum = dish.getPrice() * Integer.valueOf(quantity);

        double delta = newSum - Double.parseDouble(currentSumOfDish);

        OrderItemsJSON orderItemsJSON = new OrderItemsJSON();
        orderItemsJSON.setSum(newSum);
        orderItemsJSON.setSumTotal(Double.parseDouble(sumTotal) + delta);
        orderItemsJSON.setSumTotalOfUser(Double.parseDouble(currentSumOfUser) + delta);
        return orderItemsJSON;
    }

}
