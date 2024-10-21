package org.example;

import java.sql.Connection;
import java.util.List;

public class MealManager {

    private final DbManager dbManager;
    private final UserInterface userInterface;
    private List<Meal> allMeals, mealsByCategory;

    MealManager(Connection dbConnection) {
        this.dbManager = new DbManager(dbConnection);
        this.userInterface = new UserInterface();
        startApplication();
    }

    @SuppressWarnings("InfiniteLoopStatement") // exit() method handles the InfiniteLoopStatement
    private void startApplication() {
        loadAllMeals();
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

    private void loadAllMeals() {
        allMeals = dbManager.loadMeals("ALL MEALS");
    }

    private void loadMealsByCategory(String category) {
        mealsByCategory = dbManager.loadMeals(category);
    }

    private void addMeal() {
        Meal mealToAdd = userInterface.addMeal();
        allMeals.add(mealToAdd);
        dbManager.addMeal(mealToAdd);
    }

    private void showByCategory() {
        String selectedMealCategory = userInterface.selectMealCategory();
        loadMealsByCategory(selectedMealCategory);
        printMeals(selectedMealCategory, mealsByCategory);
    }

    private void printMeals(String mealCategory, List<Meal> mealsToShow) {
        if (mealsToShow.isEmpty()) {
            System.out.println("No meals found.");
        } else {
            System.out.println("Category: " + mealCategory);
            for (Meal meal : mealsToShow) {
                System.out.print("\n" + meal);
            }
            System.out.println();
        }
    }

    private void exit() {
        System.out.println("Bye!");
        System.exit(0);
    }

}
