package org.example.database;

import org.example.model.Meal;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MealDaoPostgresImpl implements MealDao {

    private final Connection dbConnection;

    public MealDaoPostgresImpl(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }


    @Override
    public List<List<String>> loadMeals(String category, String order) {
        String loadMealQuery;
        boolean loadByCategory = false;

        List<List<String>> mealsList = new ArrayList<>();

        if (category == null) {
            loadMealQuery = "SELECT * FROM meals;";
        } else {
            loadByCategory = true;
            loadMealQuery = "SELECT * FROM meals WHERE category = ?";
        }
        if (order != null) {
            loadMealQuery += " ORDER BY meal " + order;
        }

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(loadMealQuery)) {
            if (loadByCategory) {
                preparedStatement.setString(1, category);
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String mealCategory = resultSet.getString("category");
                    String mealName = resultSet.getString("meal");
                    int mealId = resultSet.getInt("meal_id");
                    String mealIngredients = loadMealIngredients(mealId);

                    List<String> individualMealData = new ArrayList<>();
                    individualMealData.add(mealCategory); // 0 Category
                    individualMealData.add(mealName); // 1 Meal
                    individualMealData.add(mealIngredients); // 2 Ingredients
                    individualMealData.add(Integer.toString(mealId)); // 3 Id

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
        String selectIngredientQuery = "SELECT ingredient FROM ingredients WHERE meal_id = ?";

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(selectIngredientQuery)) {
            preparedStatement.setInt(1, mealId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String ingredient = resultSet.getString("ingredient");
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

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(insertMealQuery)) {
            preparedStatement.setString(1, mealToAdd.getCategory());
            preparedStatement.setString(2, mealToAdd.getMealName());
            ResultSet generatedKeys = preparedStatement.executeQuery();

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

    @Override
    public void saveMealPlan(Map.Entry<Integer, String> mealOption, String mealCategory, String day) {
        String insertPlanQuery = "INSERT INTO plan (meal_option, meal_category, meal_id, day) VALUES (?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(insertPlanQuery)) {
            preparedStatement.setString(1, mealOption.getValue());
            preparedStatement.setString(2, mealCategory);
            preparedStatement.setInt(3, mealOption.getKey());
            preparedStatement.setString(4, day);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error saving meal plan: " + e.getMessage());
        }
    }

    @Override
    public Map<String, String> loadMealPlanByDays(String day) {
        Map<String, String> mealPlanForDay = new LinkedHashMap<>();
        String loadPlanQuery = "SELECT meal_category, meal_option FROM plan WHERE day = ?";

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(loadPlanQuery)) {
            preparedStatement.setString(1, day);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String meal_category = resultSet.getString("meal_category");
                    String meal_option = resultSet.getString("meal_option");
                    mealPlanForDay.put(meal_category, meal_option);
                }
            } catch (SQLException e) {
                System.err.println("Error loading meal plan for " + day);
            }
        } catch (SQLException e) {
            System.err.println("Error preparing statement for " + day + "'s meal plan");
        }
        return mealPlanForDay;
    }

    @Override
    public void clearOldPlan() {
        String deletePlanQuery = "DELETE FROM plan;";
        try (Statement statement = dbConnection.createStatement()) {
            statement.executeUpdate(deletePlanQuery);
        } catch (SQLException e) {
            System.err.println("Error clearing old plans: " + e.getMessage());
        }
    }

}
