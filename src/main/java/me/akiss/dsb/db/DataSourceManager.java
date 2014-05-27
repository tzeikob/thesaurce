package me.akiss.dsb.db;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

/**
 * A class implements a data source manager for a DBMS, managing connections
 * retrieved from a poolable connection factory.
 *
 * @author Akis Papadopoulos
 */
public final class DataSourceManager {

    // EPSOS global properties path
    private static final String EPSOS_PROPS_PATH = "/opt/openncp/config/";
    
    // Logger instance
    private static final Logger LOGGER;
    // External properties file
    private static final Properties PROPS;
    // Data source
    private static final DataSource DATASOURCE;
    // Connections pool
    private static final GenericObjectPool POOL;

    static {
        // Setting logger instance
        LOGGER = Logger.getLogger(DataSourceManager.class);

        // Logging a short info message
        LOGGER.info("[" + new Date() + "] " + "INFO: " + "Loading datasource connection manager.");

        // Creating an empty properties instance
        PROPS = new Properties();

        try {
            // Creating an input stream
            FileReader reader = new FileReader(EPSOS_PROPS_PATH + "nps-datasources.properties");

            // Logging a short debug message
            LOGGER.debug("[" + new Date() + "] " + "DEBUG: " + "Reading datasource properties from " + reader.toString() + ".");

            // Loading properties
            PROPS.load(reader);

            // Logging a short debug message
            LOGGER.debug("[" + new Date() + "] " + "DEBUG: " + "Datasource properties loaded successfuly.");
        } catch (Exception exc) {
            // Logging a short error message
            LOGGER.error("[" + new Date() + "] " + "ERROR: " + exc);
        }

        try {
            // Reading the datasource driver
            String driver = PROPS.getProperty("datasource.driver");

            // Loading the datasource driver
            Class.forName(driver);

            // Logging a short debug message
            LOGGER.debug("[" + new Date() + "] " + "DEBUG: " + "Datasource driver " + driver + " loaded successfuly.");
        } catch (Exception exc) {
            // Logging a short error message
            LOGGER.error("[" + new Date() + "] " + "ERROR: " + exc);
        }

        // Creating a pool of connections
        POOL = new GenericObjectPool(null);

        try {
            // Reading the minimum number of idle connections
            int minIdle = Integer.parseInt(PROPS.getProperty("datasource.minIdle", "10"));

            // Setting minimum number of idle connections
            POOL.setMinIdle(minIdle);
        } catch (Exception exc) {
            // Logging a short warning message
            LOGGER.warn("[" + new Date() + "] " + "WARN: " + exc);

            // Setting a default value
            POOL.setMinIdle(10);
        }

        try {
            // Reading the maximum number of active connections
            int maxActive = Integer.parseInt(PROPS.getProperty("datasource.maxActive", "20"));

            // Setting maximum number of active connections
            POOL.setMaxActive(maxActive);
        } catch (Exception exc) {
            // Logging a short warning message
            LOGGER.warn("[" + new Date() + "] " + "WARN: " + exc);

            // Setting a default value
            POOL.setMaxActive(20);
        }

        // Reading the database connection URL
        String url = PROPS.getProperty("datasource.url");

        // Logging a short debug message
        LOGGER.debug("[" + new Date() + "] " + "DEBUG: " + "Datasource connection URL " + url + " loaded successfuly.");

        // Reading the username
        String username = PROPS.getProperty("datasource.username");

        // Reading the password
        String password = PROPS.getProperty("datasource.password");

        // Creating a connection factory, pool use to create connections
        ConnectionFactory cf = new DriverManagerConnectionFactory(url, username, password);

        // Creating a poolable connection factory
        PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, POOL, null, null, false, true);

        // Creating a poolable data source
        DATASOURCE = new PoolingDataSource(POOL);

        // Logging a short info message
        LOGGER.info("[" + new Date() + "] " + "INFO: " + "Datasource pool loaded successfuly.");
    }

    /**
     * A method to get new data source connection.
     *
     * @return data source connection.
     * @throws Exception throw anything wrong.
     */
    public static Connection getConnection() throws Exception {
        return DATASOURCE.getConnection();
    }

    /**
     * A method returns statistical results of the connections pool.
     *
     * @return connections pool statistics.
     * @throws Exception throw anything wrong.
     */
    public static String getStats() throws Exception {
        return "NID = " + POOL.getNumIdle() + ", NAC = " + POOL.getNumActive();
    }

    /**
     * A method to clear the connections pool.
     *
     * @throws Exception throw anything wrong.
     */
    public static void clear() throws Exception {
        POOL.clear();
    }
    
    /**
     * A method closing an open result set from the datasource.
     * 
     * @param resultset the result set of the transaction.
     */
    public static void closeResultSet(ResultSet resultset) {
        try {
            if (resultset != null) {
                resultset.close();
            }
        } catch (SQLException exc) {
            LOGGER.error("An error occurred closing result set from datasource.");
            exc.printStackTrace();
        }
    }
    
    /**
     * A method closing an open statement from the datasource.
     * 
     * @param statement the statement of the transaction.
     */
    public static void closeStatement(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException exc) {
            LOGGER.error("An error occurred closing statement from datasource.");
            exc.printStackTrace();
        }
    }
    
    /**
     * A method closing an open prepared statement from the datasource.
     * 
     * @param statement the prepared statement of the transaction.
     */
    public static void closePreparedStatement(PreparedStatement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException exc) {
            LOGGER.error("An error occurred closing statement from datasource.");
            exc.printStackTrace();
        }
    }
    
    /**
     * A method closing an open connection from the datasource.
     * 
     * @param connection the connection of the transaction.
     */
    public static void closeConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
            
            // Releasing gto be available to the pooler
            connection = null;
        } catch (SQLException exc) {
            LOGGER.error("An error occurred closing datasource connection.");
            exc.printStackTrace();
        }
    }

    /**
     * A method to finalize the data source manager.
     */
    @Override
    protected void finalize() {
        try {
            super.finalize();
        } catch (Throwable exc) {
            // Logging a short error message
            LOGGER.error("[" + new Date() + "] " + "ERROR: " + exc);
        }
    }
}
