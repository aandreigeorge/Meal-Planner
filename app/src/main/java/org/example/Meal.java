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
                response = MealUtils.isValidMealCategory(value);
                if (response) {
                    this.category = value;
                }
                break;

            case "NAME":
                response = MealUtils.isValidNameOrIngredient(value);
                if (response) {
                    this.mealName = value;
                }
                break;

            case "INGREDIENTS":
                response = MealUtils.isValidNameOrIngredient(value);
                if (response) {
                    this.ingredients = Arrays.stream(value.split(","))
                            .map(String::trim)
                            .toList();
                }
                break;
        }
        return response;
    }

    @Override
    public String toString() {
        String formattedIngredients = String.join("\n", this.ingredients);
        return """
                Name: %s
                Ingredients:
                %s
                """.formatted(this.mealName, formattedIngredients);
    }

}
