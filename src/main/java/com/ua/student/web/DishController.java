package com.ua.student.web;

import com.ua.student.dao.DishDAO;
import com.ua.student.dao.UserDAO;
import com.ua.student.model.*;
import com.ua.student.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import com.ua.student.validator.DishValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class DishController {

    @Autowired
    private DishValidator dishValidator;

    @Autowired
    private DishDAO dishDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private OrderService orderService;

    @InitBinder("dishForm")
    public void myInitBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        if (target.getClass() == Dish.class) {
            dataBinder.setValidator(dishValidator);
            // For upload Image.
            dataBinder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
        }
    }

    @RequestMapping(value = "/firstDishes", method = RequestMethod.GET)
    public String firstDishes(Model model) {
        model.addAttribute("typeOfDishes", TypeDishes.firstDishes.getName());
        model.addAttribute("dishList", dishDAO.getDishListByType(TypeDishes.firstDishes));
        return "dishList";
    }

    @RequestMapping(value = "/secondDishes", method = RequestMethod.GET)
    public String secondDishes(Model model) {
        model.addAttribute("typeOfDishes", TypeDishes.secondDishes.getName());
        model.addAttribute("dishList", dishDAO.getDishListByType(TypeDishes.secondDishes));
        return "dishList";
    }

    @RequestMapping(value = "/salads", method = RequestMethod.GET)
    public String salads(Model model) {
        model.addAttribute("typeOfDishes", TypeDishes.salads.getName());
        model.addAttribute("dishList", dishDAO.getDishListByType(TypeDishes.salads));
        return "dishList";
    }

    @RequestMapping(value = "/deserts", method = RequestMethod.GET)
    public String deserts(Model model) {
        model.addAttribute("typeOfDishes", TypeDishes.deserts.getName());
        model.addAttribute("dishList", dishDAO.getDishListByType(TypeDishes.deserts));
        return "dishList";
    }

    @RequestMapping(value = "/drinks", method = RequestMethod.GET)
    public String drinks(Model model) {
        model.addAttribute("typeOfDishes", TypeDishes.drinks.getName());
        model.addAttribute("dishList", dishDAO.getDishListByType(TypeDishes.drinks));
        return "dishList";
    }

    @RequestMapping(value = {"/delete"}, method = RequestMethod.GET)
    public String delete(Model model, @RequestParam(value = "id") String id) {
        TypeDishes currentTypeDishes = dishDAO.getDishById(Integer.valueOf(id)).getTypeDishes();
        dishDAO.delete(Integer.valueOf(id));
        model.addAttribute("dishList", dishDAO.getDishListByType(currentTypeDishes));
        return "redirect:/" + currentTypeDishes.name();
    }

    @RequestMapping(value = {"/dishImage"}, method = RequestMethod.GET)
    public void dishImage(HttpServletResponse response, @RequestParam("id") String id) throws IOException {
        Dish dish = null;
        if (id != null) {
            dish = dishDAO.getDishById(Integer.valueOf(id));
        }
        if (dish != null && dish.getImage() != null) {
            response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
            response.getOutputStream().write(dish.getImage());
        }
        response.getOutputStream().close();
    }

    @RequestMapping(value = {"/editDish"}, method = RequestMethod.GET)
    public String dish(Model model, @RequestParam(value = "id", defaultValue = "") String id) {

        Dish dish = null;

        if (id != null && id.length() > 0) {
            dish = dishDAO.getDishById(Integer.valueOf(id));
        }
        if (dish == null) {
            dish = new Dish();
        }

        model.addAttribute("typeDishes", TypeDishes.values());
        model.addAttribute("dishForm", dish);
        return "editDish";
    }

    @RequestMapping(value = {"/editDish"}, method = RequestMethod.POST)
    public String dish(@ModelAttribute("dishForm") @Validated Dish dish, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "editDish";
        }

        if (dish.getFileData() != null) {
            byte[] image = dish.getFileData().getBytes();
            if (image != null && image.length > 0) {
                dish.setImage(image);
            } else if (dish.getId() != 0) {
                dish.setImage(dishDAO.getDishById(dish.getId()).getImage());
            }
        }

        if (dish.getId() == 0) {
            dishDAO.insert(dish);
        } else {
            dishDAO.update(dish);
        }

        return "redirect:/" + dish.getTypeDishes().name();
    }

    @RequestMapping({"/orderDish"})
    public String orderDish(HttpServletRequest request,
                            @RequestParam(value = "id", defaultValue = "") String id,
                            Authentication authentication) {

        Dish dish = null;
        if (id.length() > 0) {
            dish = dishDAO.getDishById(Integer.valueOf(id));
        }

        if (dish != null) {

            Order order = orderService.getOrderInSession(request);
            User currentUser = userDAO.getUserByUsername(authentication.getName());

            OrderItems item = new OrderItems();
            item.setQuantity(1);
            item.setDish(dish);
            item.setUser(currentUser);
            item.setPrice(dish.getPrice());
            orderService.addDish(item, order.getOrderItems());
        }
        return "redirect:/basket";
    }

    @RequestMapping({"/removeDish"})
    public String removeDish(HttpServletRequest request,
                             Authentication authentication,
                             @RequestParam String id) {

        Dish dish = null;
        if (id != null && id.length() > 0) {
            dish = dishDAO.getDishById(Integer.valueOf(id));
        }
        if (dish != null) {
            Order order = orderService.getOrderInSession(request);
            orderService.removeDish(userDAO.getUserByUsername(authentication.getName()), dish, order.getOrderItems());
        }

        return "redirect:/basket";
    }
}