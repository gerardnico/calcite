package com.gerardnico.calcite;

import org.apache.calcite.jdbc.CalciteConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Snippet of code with the model
 */
public class CalciteModel {

    /**
     * The model path can be given as a property
     *
     * @throws ClassNotFoundException
     */
    public static CalciteConnection getConnectionFromModel(String modelPath)  {
        try {

            Properties properties = new Properties();
            properties.setProperty("model", modelPath);
            Class.forName("org.apache.calcite.jdbc.Driver");

            Connection connection = DriverManager.getConnection("jdbc:calcite:", properties);
            return connection.unwrap(CalciteConnection.class);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
