package org.example.database;

import org.example.model.Meal;

import java.util.List;

public interface MealDao {

    void addMeal(Meal meal);

    List<List<String>> loadMeals(String category);
}
