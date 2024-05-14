package database;

import lombok.Getter;

import java.sql.*;

public class DB_Database {
    @Getter
    private Connection connection;

    private static String DB_URL;
    private static String DB_USERNAME;
    private static String DB_PASSWD;
    private static String DB_DRIVER;
    private DB_StatementPool pool;

    // Singleton
    private static DB_Database instance = null;

    private DB_Database() {
        // load properties and connect
        loadProperties();
        try {
            Class.forName(DB_DRIVER);
            connect();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("DB error: " + e.getMessage());
        }
    }

    public static DB_Database getInstance() {
        if (instance == null) {
            instance = new DB_Database();
        }
        return instance;
    }

    /**
     * Load db connection properties
     */
    public void loadProperties() {
        DB_URL = DB_Properties.getProperty("DB_URL");
        DB_USERNAME = DB_Properties.getProperty("DB_USER");
        DB_PASSWD = DB_Properties.getProperty("DB_PASSWORD");
        DB_DRIVER = DB_Properties.getProperty("DB_DRIVER");
    }

    private void connect() throws SQLException {
        if (connection != null) {
            connection.close();
        }
        connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWD);
        pool = new DB_StatementPool(connection);
    }

    public void close() throws SQLException {
        if (pool != null) {
            pool.closeStatements();
            pool = null;
        }

        if (connection != null) {
            connection.close();
            connection = null;
        }

        instance = null;
    }

    public Statement getStatement() throws SQLException {
        if(pool != null && connection != null) {
            return pool.getStatement();
        }
        throw new RuntimeException("not connected");
    }

    public void releaseStatement(Statement statement) {
        if(pool != null && connection != null) {
            pool.releaseStatement(statement);
        } else {
            throw new RuntimeException("Something wrong lol");
        }
    }
}
