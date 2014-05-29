package me.akiss.dsb.db;

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
            logger.error("An unknown error occurred creating a statement: '" + exc.getMessage() + "'.");
        }

        return statement;
    }

    /**
     * A method releasing a prepared statement resource.
     *
     * @param statement the prepared statement resource.
     */
    public void closeStatement(PreparedStatement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException exc) {
            logger.error("An unknown error occurred closing  statement: '" + exc.getMessage() + "'.");
        }
    }

    /**
     * A method releasing a result set resource.
     *
     * @param resultSet the result set resource.
     */
    public void closeResultSet(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException exc) {
            logger.error("An unknown error occurred closing a result set: '" + exc.getMessage() + "'.");
        }
    }

    /**
     * A method closing an open connection session.
     */
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException exc) {
            logger.error("An unknown error occured closing a connection session: '" + exc.getMessage() + "'.");
        }
    }
}
