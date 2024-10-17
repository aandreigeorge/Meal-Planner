package org.example;

import java.util.Arrays;
import java.util.List;

public class Meal {

    private String category;
    private String mealName;
    private List<String> ingredients;


    String getCategory() {
        return this.category;
    }

    String getMealName() {
        return this.mealName;
    }

    List<String> getIngredients() {
        return this.ingredients;
    }

    boolean setFields(String field, String value) {
        field = field.toUpperCase();
        boolean response = true;

        switch (field) {
            case "CATEGORY":
                response = validateMealCategory(value);
                if (response) {
                    this.category = value;
                }
                break;

            case "NAME":
                response = validateNameOrIngredients(value);
                if (response) {
                    this.mealName = value;
                }
                break;

            case "INGREDIENTS":
                response = validateNameOrIngredients(value);
                if (response) {
                    this.ingredients = Arrays.stream(value.split(","))
                            .map(String::trim)
                            .toList();
                }
                break;
        }
        return response;
    }

    private boolean validateMealCategory(String nameOrCategory) {
        return nameOrCategory.equalsIgnoreCase("breakfast") ||
                nameOrCategory.equalsIgnoreCase("lunch") ||
                nameOrCategory.equalsIgnoreCase("dinner");
    }

    private boolean validateNameOrIngredients(String inputToValidate) {
        String pattern = "[A-Za-z -]+";
        String[] nameOrIngredients = inputToValidate.split(",");

        for (String string : nameOrIngredients) {
            if (string.isEmpty() || !string.trim().matches(pattern)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        String formattedIngredients = String.join("\n", this.ingredients);
        return """
                Category: %s
                Name: %s
                Ingredients:
                %s
                """.formatted(this.category, this.mealName, formattedIngredients);
    }

}
