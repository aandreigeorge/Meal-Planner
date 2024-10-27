package org.example.model;

import java.util.Arrays;
import java.util.List;

public class Meal {

    private String mealCategory;
    private String mealName;
    private List<String> mealIngredients;

    public Meal(String mealCategory, String mealName, String mealIngredients) {
        setCategory(mealCategory);
        setMealName(mealName);
        setMealIngredients(mealIngredients);
    }


    public String getCategory() {
        return this.mealCategory;
    }

    public String getMealName() {
        return this.mealName;
    }

    public List<String> getIngredients() {
        return this.mealIngredients;
    }

    public void setCategory(String category) {
        this.mealCategory = category;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public void setMealIngredients(String mealIngredients) {
        this.mealIngredients = Arrays.stream(mealIngredients.split(",")).map(String::trim).toList();
    }

    @Override
    public String toString() {
        String formattedIngredients = String.join("\n", this.mealIngredients);
        return """
                Name: %s
                Ingredients:
                %s
                """.formatted(this.mealName, formattedIngredients);
    }

}
