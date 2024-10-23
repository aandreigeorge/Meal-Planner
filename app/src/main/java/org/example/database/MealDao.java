package org.example.database;

import org.example.model.Meal;

import java.util.List;
import java.util.Map;

public interface MealDao {

    void addMeal(Meal meal);

    List<List<String>> loadMeals(String category);

    void saveMealPlan(Map.Entry<Integer, String> mealOption, String mealCategory, String day);

    Map<String, String> loadMealPlanByDays(String day);

    void clearOldPlan();

}
