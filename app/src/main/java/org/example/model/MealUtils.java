package org.example.model;

public class MealUtils {

    public static boolean isValidMealCategory(String nameOrCategory) {
        return nameOrCategory.equalsIgnoreCase("BREAKFAST") ||
                nameOrCategory.equalsIgnoreCase("LUNCH") ||
                nameOrCategory.equalsIgnoreCase("DINNER");
    }

    public static boolean isValidNameOrIngredient(String inputToValidate) {
        String pattern = "[A-Za-z -]+";
        String[] nameOrIngredients = inputToValidate.split(",");

        for (String string : nameOrIngredients) {
            if (string.isEmpty() || !string.trim().matches(pattern)) {
                return false;
            }
        }
        return true;
    }
}
