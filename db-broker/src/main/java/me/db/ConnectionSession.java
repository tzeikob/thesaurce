package me.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 * A data source connection session.
 *
 * @author Akis Papadopoulos, iakopap@gmail.com
 */
public class ConnectionSession {

    // Logger
    private static final Logger logger = Logger.getLogger(ConnectionSession.class);
    
    // Datasource connection
    private Connection connection;

    /**
     * A constructor creating a connection session given a JDBC connection.
     *
     * @param connection the database connection.
     */
    public ConnectionSession(Connection connection) {
        this.connection = connection;
    }

    /**
     * A method returning prepared statement given the SQL query.
     *
     * @return the prepared statement.
     */
    public PreparedStatement getStatement(String query) {
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(query);
        } catch (SQLException exc) {
            logger.error("An SQL error occurred creating a statement: '" + exc.getMessage() + "'.");
        } catch (NullPointerException exc) {
            logger.error("An error occurred creating a statement using a null flavored connection: '" + exc.getMessage() + "'.");
        } catch (Exception exc) {
            logger.error("An unknown error occurred creating a statement: '" + exc.getMessage() + "'.");
        }

        return statement;
    }

    /**
     * A method closing an open connection session given the related statement
     * and result set resources.
     *
     * @param statement the prepared statement resource.
     * @param resultSet the result set resource.
     */
    public void close(PreparedStatement statement, ResultSet resultSet) {
        try {
            if (resultSet != null && !resultSet.isClosed()) {
                resultSet.close();
            }

            if (statement != null && !statement.isClosed()) {
                statement.close();
            }

            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException exc) {
            logger.error("An SQL error occured closing a connection session: '" + exc.getMessage() + "'.");
        } catch (Exception exc) {
            logger.error("An unknown error occured closing a connection session: '" + exc.getMessage() + "'.");
        }
    }
    
    /**
     * A method returning the state of the connection session.
     * 
     * @return true if the session is closed, otherwise false.
     */
    public boolean isClosed() {
        boolean closed = true;
        
        try {
            closed =  connection.isClosed();
        } catch (SQLException exc) {
            logger.error("An SQL error occured checking if a connection session is closed: '" + exc.getMessage() + "'.");
        } catch (NullPointerException exc) {
            logger.error("An error occured checking if a null flavored connection session is closed: '" + exc.getMessage() + "'.");
        } catch (Exception exc) {
            logger.error("An unknown error occured checking if a connection session is closed: '" + exc.getMessage() + "'.");
        }
        
        return closed;
    }
}
