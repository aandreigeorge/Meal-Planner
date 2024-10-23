package org.example.service;

import org.example.database.MealDao;
import org.example.database.MealDaoPostgresImpl;
import org.example.model.Meal;
import org.example.model.MealFactory;
import org.example.model.SimpleMealFactory;
import org.example.ui.UserInterface;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class MealManager {

    private final MealDao mealDaoPostgres;
    private final UserInterface userInterface;
    private final MealFactory simpleMealFactory;

    public MealManager(Connection dbConnection) {
        this.simpleMealFactory = new SimpleMealFactory();
        this.userInterface = new UserInterface();
        this.mealDaoPostgres = new MealDaoPostgresImpl(dbConnection);
        startApp();
    }

    @SuppressWarnings("InfiniteLoopStatement") // exit() method handles the InfiniteLoopStatement
    private void startApp() {
        String action;

        do {
            action = userInterface.getMenuChoice();

            switch (action) {
                case "ADD" -> addMeal();
                case "SHOW" -> showByCategory();
                case "EXIT" -> exit();
            }
        } while (true);
    }

    private void addMeal() {
        List<String> mealData = userInterface.getMealFromUser();
        String mealCategory = mealData.get(0);
        String mealName = mealData.get(1);
        String mealIngredients = mealData.get(2);
        Meal mealToAdd = simpleMealFactory.createMeal(mealCategory, mealName, mealIngredients);
        mealDaoPostgres.addMeal(mealToAdd);
    }

    private void showByCategory() {
        String category = userInterface.selectMealCategoryToShow();
        List<List<String>> mealData = mealDaoPostgres.loadMeals(category);
        List<Meal> meals = constructMeals(mealData);
        System.out.println();
        meals.forEach(System.out::println);
    }

    private List<Meal> constructMeals(List<List<String>> mealsData) {
        List<Meal> meals = new ArrayList<>();

        for (List<String> mealEntry : mealsData) {
            String mealCategory = mealEntry.get(0);
            String mealName = mealEntry.get(1);
            String mealIngredients = mealEntry.get(2);
            Meal meal = simpleMealFactory.createMeal(mealCategory, mealName, mealIngredients);
            meals.add(meal);
        }

        return meals;
    }

    private void exit() {
        System.out.println("Bye!");
        System.exit(0);
    }
}
