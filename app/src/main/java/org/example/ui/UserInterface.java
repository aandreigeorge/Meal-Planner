package org.example.ui;

import org.example.utils.MealUtils;
import org.example.utils.TextUtils;

import java.util.List;
import java.util.Scanner;

public class UserInterface {

    private static final Scanner scanner = new Scanner(System.in);

    public String getMenuChoice() {
        System.out.println("What would you like to do (add, show, plan, list plan, exit)?");
        return scanner.nextLine().toUpperCase();
    }

    public List<String> getMealDataFromUser() {
        String mealCategory = promptForMealCategory();
        String mealName = promptForMealNameOrIngredients("MEAL NAME");
        String mealIngredients = promptForMealNameOrIngredients("MEAL INGREDIENTS");
        return List.of(mealCategory, mealName, mealIngredients);
    }

    private String promptForMealCategory() {
        String userInput;
        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        do {
            userInput = scanner.nextLine().toLowerCase();
            if (!MealUtils.isValidMealCategory(userInput)) {
                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            }
        } while (!MealUtils.isValidMealCategory(userInput));

        return userInput;
    }

    private String promptForMealNameOrIngredients(String selection) {
        String userInput;

        if (selection.equalsIgnoreCase("MEAL NAME")) {
            System.out.println("Input the meal's name: ");
        } else if (selection.equalsIgnoreCase("MEAL INGREDIENTS")) {
            System.out.println("Input the ingredients: ");
        }

        do {
            userInput = TextUtils.toTitleCase(scanner.nextLine());
            if (!MealUtils.isValidNameOrIngredient(userInput)) {
                System.out.println("Wrong format. Use letters only!");
            }
        } while (!MealUtils.isValidNameOrIngredient(userInput));

        return userInput;
    }

    public String selectCategoryOfMealsToShow() {
        String mealCategory;
        System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");

        do {
            mealCategory = scanner.nextLine().toLowerCase();
            if (!MealUtils.isValidMealCategory(mealCategory)) {
                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            }
        } while (!MealUtils.isValidMealCategory(mealCategory));

        return mealCategory;
    }

    public String getMealChoiceForPlan() {
        return scanner.nextLine();
    }

    public void closeScanner() {
        scanner.close();
    }
}
