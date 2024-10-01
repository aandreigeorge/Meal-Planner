import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Meal {

    private static final Scanner scanner = new Scanner(System.in);

    private String category, mealName;
    private List<String> ingredients;

    Meal() {
        createMeal();
        printMeal();
    }

    private void createMeal() {

        System.out.println("Which meal do you want to add (breakfast, lunch, dinner)?");
        category = scanner.nextLine();
        System.out.println("Input the meal's name: ");
        mealName = scanner.nextLine();
        System.out.println("Input the ingredients: ");
        String userIngredients = scanner.nextLine();
        ingredients = new ArrayList<>(Arrays.asList(userIngredients.split(",")));
    }

    private void printMeal() {

        System.out.println("\nCategory: " + category);
        System.out.println("Name: " + mealName);
        System.out.println("Ingredients: ");

        for (String ingredient : ingredients) {
            System.out.println(ingredient);
        }
        System.out.println("The meal has been added!");
    }
}
