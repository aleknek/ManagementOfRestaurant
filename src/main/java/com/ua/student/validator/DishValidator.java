package com.ua.student.validator;

import com.ua.student.model.Dish;
import com.ua.student.model.TypeDishes;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class DishValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == Dish.class;
    }

    @Override
    public void validate(Object target, Errors errors) {

        Dish dish = (Dish) target;

        if (dish.getName().isEmpty()) {
            errors.rejectValue("name", "name.empty", "Значение реквизита 'Наименование' обязательно к заполнению!");
        }

        if (dish.getPrice() == 0.0) {
            errors.rejectValue("price", "price.empty", "Значение реквизита 'Цена' обязательно к заполнению!");
        }

        if (dish.getTypeDishes() == TypeDishes.NONE) {
            errors.rejectValue("typeDishes", "typeDishes.empty", "Значение реквизита 'Тип блюда' обязательно к заполнению!");
        }
    }
}