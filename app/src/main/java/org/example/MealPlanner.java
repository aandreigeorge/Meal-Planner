package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MealPlanner {

    private static final Scanner scanner = new Scanner(System.in);
    private List<Meal> meals;

    MealPlanner() {
        runMealPlanner();
    }

    @SuppressWarnings("InfiniteLoopStatement") /*exit() method handles the InfiniteLoopStatement */
    private void runMealPlanner() {

        this.meals = new ArrayList<>();
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

        Meal mealToAdd = new Meal();

        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        constructMeal("CATEGORY", mealToAdd);

        System.out.println("Input the meal's name: ");
        constructMeal("NAME", mealToAdd);

        System.out.println("Input the ingredients: ");
        constructMeal("INGREDIENTS", mealToAdd);

        System.out.println("The meal has been added!");
        this.meals.add(mealToAdd);
    }

    private void constructMeal(String field, Meal meal) {

        boolean response;
        String errorMessage1 = "Wrong meal category! Choose from: breakfast, lunch, dinner.";
        String errorMessage2 = "Wrong format. Use letters only!";

        switch (field) {
            case "CATEGORY":
                response = meal.setFields(field, scanner.nextLine());
                if (!response) {
                    System.out.println(errorMessage1);
                    constructMeal(field, meal);
                }
                break;

            case "NAME":
                response = meal.setFields(field, scanner.nextLine());
                if (!response) {
                    System.out.println(errorMessage2);
                    constructMeal(field, meal);
                }
                break;

            case "INGREDIENTS":
                response = meal.setFields("INGREDIENTS", scanner.nextLine());
                if (!response) {
                    System.out.println(errorMessage2);
                    constructMeal(field, meal);
                }
                break;
        }
    }

    private void displayMeals() {

        if (this.meals.isEmpty()) {
            System.out.println("No meals saved. Add a meal first.");
        } else {
            for (Meal meal : this.meals) {
                System.out.println(meal);
            }
        }
    }

    private void exit() {
        System.out.println("Bye!");
        System.exit(0);
    }


}
