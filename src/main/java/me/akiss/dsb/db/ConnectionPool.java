package me.akiss.dsb.db;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

/**
 * A poolable connection manager.
 *
 * @author Akis Papadopoulos, iakopap@gmail.com
 */
public class ConnectionPool implements ConnectionManager {

    // Logger
    private static final Logger logger = Logger.getLogger(ConnectionPool.class);
    
    // Data source
    private DataSource datasource;
    
    // Connections pool
    private GenericObjectPool pool;

    /**
     * A constructor creating a poolable connection manager.
     *
     * @param jdbcDriver the JDBC driver to load.
     * @param jdbcUrl the database URL to connect.
     * @param username the database username.
     * @param password the database password.
     */
    public ConnectionPool(String jdbcDriver, String jdbcUrl, String username, String password) {
        try {
            // Loading the JDBC driver
            Class.forName(jdbcDriver);

            // Creating a pool of connections
            pool = new GenericObjectPool(null);

            // Setting min/max number of idle/active connections
            pool.setMinIdle(10);
            pool.setMaxActive(20);

            // Creating a poolable data source
            ConnectionFactory cf = new DriverManagerConnectionFactory(jdbcUrl, username, password);

            PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, pool, null, null, false, true);

            datasource = new PoolingDataSource(pool);
            
            logger.info("Connection manager loaded successfully linked to database '" + jdbcUrl + "'.");
        } catch (ClassNotFoundException exc) {
            logger.error("An error occurred loading the JDBC driver identified by '" + jdbcDriver + "'.");
        } catch (Exception exc) {
            logger.error("An error occurred setting up a connection manager for the database '" + jdbcUrl + "'.");
        }
    }

    /**
     * A method setting up the number of minimum idle connections.
     *
     * @param minIdleConnections the number of minimum idle connections.
     */
    @Override
    public void setMinIdleConnections(int minIdleConnections) {
        pool.setMinIdle(minIdleConnections);
    }

    /**
     * A method setting up the number of maximum active connections.
     *
     * @param maxActiveConnections the number of maximum active connections.
     */
    @Override
    public void setMaxActiveConnections(int maxActiveConnections) {
        pool.setMaxActive(maxActiveConnections);
    }

    /**
     * A method returning a new connection session.
     *
     * @return the connection session to return.
     */
    @Override
    public ConnectionSession getSession() {
        ConnectionSession session = null;

        try {
            session = new ConnectionSession(datasource.getConnection());
        } catch (SQLException exc) {
            logger.error("An error occurred getting a new connection session '" + exc.getMessage() + "'.");
        }

        return session;
    }

    /**
     * A method resetting the connection manager.
     */
    @Override
    public void reset() {
        pool.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("[ConnectionManager ")
                .append("NIC: '").append(pool.getNumIdle()).append("', ")
                .append("NAC: '").append(pool.getNumActive()).append("']");

        return sb.toString();
    }

    @Override
    protected void finalize() {
        try {
            super.finalize();
        } catch (Throwable exc) {
            logger.error("An error occurred finalizing the connection manager.");
        }
    }
}
