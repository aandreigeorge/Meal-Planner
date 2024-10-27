package org.example.database;

import org.example.model.Meal;

import java.util.List;
import java.util.Map;

public interface MealDao {

    void addMeal(Meal meal);

    List<List<String>> loadMeals(String category, String order);

    List<String> loadMealIngredients(int mealId);

    void saveMealToPlan(Map.Entry<Integer, String> mealOption, String mealCategory, String day);

    Map<String, String> loadMealPlanByDays(String day);

    Map<Integer, Integer> getMealCountsFromPlan();

    void clearOldMealPlan();

}
