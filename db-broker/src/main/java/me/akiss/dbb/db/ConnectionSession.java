package me.akiss.dbb.db;

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
            if(!connection.isClosed()) {
                statement = connection.prepareStatement(query);
            } else {
                logger.warn("Failed to create a statement from a closed connection session.");
            }
        } catch (SQLException exc) {
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
            logger.error("An unknown error occured checking if a connection session is closed: '" + exc.getMessage() + "'.");
        }
        
        return closed;
    }
}
