package org.example.ui;

import org.example.model.MealUtils;

import java.util.List;
import java.util.Scanner;

public class UserInterface {

    private static final Scanner scanner = new Scanner(System.in);

    public String getMenuChoice() {
        System.out.println("What would you like to do (add, show, plan, list plan, exit)?");
        return scanner.nextLine().toUpperCase();
    }

    public List<String> getMealFromUser() {
        String mealCategory = getMealCategory();
        String mealName = getMealNameOrIngredients("MEAL NAME");
        String mealIngredients = getMealNameOrIngredients("MEAL INGREDIENTS");
        System.out.println("The meal has been added!");
        return List.of(mealCategory, mealName, mealIngredients);
    }

    private String getMealCategory() {
        String userInput;
        System.out.println("Which meal category do you want to add (breakfast, lunch, dinner)?");
        do {
            userInput = scanner.nextLine();
            if (!MealUtils.isValidMealCategory(userInput)) {
                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            }
        } while (!MealUtils.isValidMealCategory(userInput));

        return userInput;
    }

    private String getMealNameOrIngredients(String selection) {
        String userInput;

        if (selection.equalsIgnoreCase("MEAL NAME")) {
            System.out.println("Input the meal's name: ");
        } else if (selection.equalsIgnoreCase("MEAL INGREDIENTS")) {
            System.out.println("Input the ingredients: ");
        }

        do {
            userInput = scanner.nextLine();
            if (!MealUtils.isValidNameOrIngredient(userInput)) {
                System.out.println("Wrong format. Use letters only!");
            }
        } while (!MealUtils.isValidNameOrIngredient(userInput));

        return userInput;
    }

    public String selectMealCategoryToShow() {
        String mealCategory;
        System.out.println("Which category do you want to print (breakfast, lunch, dinner)?");

        do {
            mealCategory = scanner.nextLine();
            if (!MealUtils.isValidMealCategory(mealCategory)) {
                System.out.println("Wrong meal category! Choose from: breakfast, lunch, dinner.");
            }
        } while (!MealUtils.isValidMealCategory(mealCategory));

        return mealCategory;
    }
}
