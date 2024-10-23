package org.example.model;

public interface MealFactory {
    Meal createMeal(String category, String mealName, String ingredients);
}
