package org.example;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MealManager {

    private static final Scanner scanner = new Scanner(System.in);
    private List<Meal> mealsList;
    private final DbManager dbManager;
    private final UserInterface userInterface;

    MealManager(Connection dbConnection) {
        this.mealsList = new ArrayList<>();
        this.userInterface = new UserInterface();
        this.dbManager = new DbManager(dbConnection);
        loadMeals();
        runMeals();
    }

    private void loadMeals() {
        mealsList = dbManager.loadMeals();
    }

    @SuppressWarnings("InfiniteLoopStatement") // exit() method handles the InfiniteLoopStatement
    private void runMeals() {
        String action;

        do {
            System.out.println("What would you like to do (add, show, exit)?");
            action = scanner.nextLine().toUpperCase();

            switch (action) {
                case "ADD" -> addMeal();
                case "SHOW" -> displayMeals();
                case "EXIT" -> exit();
            }
        } while (true);
    }

    private void addMeal() {
        Meal mealToAdd = userInterface.addMeal();
        mealsList.add(mealToAdd); // Adding to local list
        dbManager.addMeal(mealToAdd); // Adding to db
    }

    private void displayMeals() {
        if (mealsList.isEmpty()) {
            System.err.println("No meals saved. Add a meal first.");
        } else {
            for (Meal meal : mealsList) {
                System.out.println(meal);
            }
            System.out.println();
        }
    }

    private void exit() {
        System.out.println("Bye!");
        System.exit(0);
    }

}
