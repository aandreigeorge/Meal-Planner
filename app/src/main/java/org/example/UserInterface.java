package org.example;

import java.util.Scanner;

public class UserInterface {

    private static final Scanner scanner = new Scanner(System.in);


    Meal addMeal() {
        Meal mealToAdd = new Meal();

        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        constructMeal("CATEGORY", mealToAdd);

        System.out.println("Input the meal's name: ");
        constructMeal("NAME", mealToAdd);

        System.out.println("Input the ingredients: ");
        constructMeal("INGREDIENTS", mealToAdd);

        System.out.println("The meal has been added!");

        return mealToAdd;
    }

    private void constructMeal(String field, Meal meal) {
        boolean response;
        String errorMessage1 = "Wrong meal category! Choose from: breakfast, lunch, dinner.";
        String errorMessage2 = "Wrong format. Use letters only!";

        switch (field) {
            case "CATEGORY":
                response = meal.setFields(field, scanner.nextLine());
                while (!response) {
                    System.err.println(errorMessage1);
                    response = meal.setFields(field, scanner.nextLine());
                }
                break;

            case "NAME":
                response = meal.setFields(field, scanner.nextLine());
                while (!response) {
                    System.err.println(errorMessage2);
                    response = meal.setFields(field, scanner.nextLine());
                }
                break;

            case "INGREDIENTS":
                response = meal.setFields("INGREDIENTS", scanner.nextLine());
                while (!response) {
                    System.err.println(errorMessage2);
                    response = meal.setFields(field, scanner.nextLine());
                }
                break;
        }
    }

}
