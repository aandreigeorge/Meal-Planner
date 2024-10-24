package org.example.app;

import org.example.service.MealManager;
import org.example.utils.DbUtils;

import java.sql.Connection;
import java.sql.SQLException;

public class App {

    public static void main(String[] args) {
        try (Connection dbConnection = DbUtils.getDbConnection()) {
            DbUtils.checkDbTableStructure(dbConnection);
            new MealManager(dbConnection);
        } catch (SQLException e) {
            System.err.println("Database Connection Failed!");
        }
    }

    public String getGreeting() {
        return "Hello World!";
    }
}
