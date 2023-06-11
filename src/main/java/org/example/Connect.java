package org.example;

import java.sql.*;

/**
 * Utility class for connecting to a PostgreSQL database and performing database operations.
 */
public class Connect {

    private String driver = "org.postgresql.Driver";
    private String host = "195.150.230.208";
    private String port = "5432";
    private String dbname = "2023_polak_michal";
    private String user = "2023_polak_michal";
    private String url = "jdbc:postgresql://" + host+":"+port + "/" + dbname;
    private String pass = "35229";
    private Connection connection;

    /**
     * Constructor for the Connect class. Establishes a database connection.
     */
    public Connect () {
        connection = makeConnection();
    }

    /**
     * Retrieves the established database connection.
     *
     * @return The Connection object representing the database connection.
     */
    public Connection getConnection(){
        return(connection);
    }

    /**
     * Closes the database connection.
     */
    public void close() {
        try {
            connection.close();
        } catch (SQLException sqle) {
            System.err.println("Blad przy zamykaniu polaczenia: " + sqle);
        }
    }

    /**
     * Creates a database connection using the specified driver, URL, username, and password.
     *
     * @return The Connection object representing the database connection.
     */
    public Connection makeConnection(){
        try {
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, user, pass);
            return(connection);
        } catch(ClassNotFoundException cnfe) {
            System.err.println("Blad ladowania sterownika: " + cnfe);
            return(null);
        } catch(SQLException sqle) {
            System.err.println("Blad przy nawiązywaniu polaczenia: " + sqle);
            return(null);
        }
    }

    /**
     * Executes a SELECT query on the database and returns the result set.
     *
     * @param query      The SELECT query to be executed.
     * @param connection The database connection to use.
     * @return The ResultSet object containing the query results.
     */
    public ResultSet select( String query, Connection connection){
        ResultSet result = null;
        try {
            Statement statement = connection.createStatement();
            result = statement.executeQuery(query);
            System.out.println("Poprawnie pobrano dane z tabeli!");
        } catch (SQLException e) {
            System.out.println("Błąd przy pobieraniu danych!");
        }
        return result;
    }

    /**
     * Executes an INSERT query on the database to add data to a table.
     *
     * @param query      The INSERT query to be executed.
     * @param connection The database connection to use.
     */
    public void insert(String query, Connection connection) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Poprawnie dodano dane do tabeli!");
        } catch (SQLException e) {
            System.out.println("Błąd przy dodawaniu danych: " + e);
        }
    }

    /**
     * Executes an UPDATE query on the database to modify data in a table.
     *
     * @param query      The UPDATE query to be executed.
     * @param connection The database connection to use.
     */
    public void update(String query, Connection connection) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("Poprawnie zmieniono dane!");
        } catch (SQLException e) {
            System.out.println("Błąd przy zmianie danych: " + e);
        }
    }
}
