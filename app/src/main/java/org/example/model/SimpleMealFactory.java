package org.example.model;

public class SimpleMealFactory implements MealFactory {

    @Override
    public Meal createMeal(String mealCategory, String mealName, String mealIngredients) {
        return new Meal(mealCategory, mealName, mealIngredients);
    }
}
