package org.example.database;

import org.example.model.Meal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MealDaoPostgresImpl implements MealDao {

    private final Connection dbConnection;

    public MealDaoPostgresImpl(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<List<String>> loadMeals(String category) {
        String query;
        boolean loadByCategory = false;
        List<List<String>> mealsList = new ArrayList<>();

        if (category.equalsIgnoreCase("ALL MEALS")) {
            query = "SELECT * FROM meals;";
        } else {
            loadByCategory = true;
            query = "SELECT * FROM meals WHERE category = ?;";
        }

        try (PreparedStatement loadMeal = dbConnection.prepareStatement(query)) {
            if (loadByCategory) {
                loadMeal.setString(1, category);
            }

            try (ResultSet mealsResultSet = loadMeal.executeQuery()) {
                while (mealsResultSet.next()) {
                    String mealCategory = mealsResultSet.getString("category");
                    String mealName = mealsResultSet.getString("meal");
                    int mealId = mealsResultSet.getInt("meal_id");
                    String mealIngredients = loadMealIngredients(mealId);

                    List<String> individualMealData = new ArrayList<>();
                    individualMealData.add(mealCategory);
                    individualMealData.add(mealName);
                    individualMealData.add(mealIngredients);

                    mealsList.add(individualMealData);
                }
            } catch (SQLException e) {
                System.err.println("Error retrieving meals for category: " + category);
            }
        } catch (SQLException e) {
            System.err.println("Error preparing statement: " + e.getMessage());
        }

        return mealsList;
    }

    private String loadMealIngredients(int mealId) {
        List<String> ingredientsList = new ArrayList<>();
        String ingredientQuery = "SELECT ingredient FROM ingredients WHERE meal_id = ?";

        try (PreparedStatement ingredientStatement = dbConnection.prepareStatement(ingredientQuery)) {
            ingredientStatement.setInt(1, mealId);

            try (ResultSet ingredientsResultSet = ingredientStatement.executeQuery()) {
                while (ingredientsResultSet.next()) {
                    String ingredient = ingredientsResultSet.getString("ingredient");
                    ingredientsList.add(ingredient);
                }
            } catch (SQLException e) {
                System.err.println("Error retrieving ingredients for meal ID: " + mealId);
                return null;
            }

        } catch (SQLException e) {
            System.err.println("Error preparing statement for meal ID: " + e.getMessage());
        }

        return String.join(",", ingredientsList);
    }

    @Override
    public void addMeal(Meal mealToAdd) {
        String insertMealQuery = "INSERT INTO meals (category, meal) VALUES (?, ?) RETURNING meal_id;";

        try (PreparedStatement insertMeal = dbConnection.prepareStatement(insertMealQuery)) {
            insertMeal.setString(1, mealToAdd.getCategory());
            insertMeal.setString(2, mealToAdd.getMealName());
            ResultSet generatedKeys = insertMeal.executeQuery();

            if (generatedKeys.next()) {
                int mealId = generatedKeys.getInt(1);
                addMealIngredients(mealId, mealToAdd.getIngredients());
            }
        } catch (SQLException e) {
            System.err.println("Error adding meal: " + e.getMessage());
        }
    }

    private void addMealIngredients(int mealId, List<String> ingredients) {
        String insertIngredientQuery = "INSERT INTO ingredients (meal_id, ingredient) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(insertIngredientQuery)) {
            for (String ingredient : ingredients) {
                preparedStatement.setInt(1, mealId);
                preparedStatement.setString(2, ingredient);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error adding ingredients for meal ID: " + e.getMessage());
        }
    }
}
