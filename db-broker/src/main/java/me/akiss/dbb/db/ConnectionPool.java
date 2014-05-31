package me.akiss.dbb.db;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;

/**
 * A poolable JDBC connection manager implementation.
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
     * @param url the database URL to connect.
     * @param username the database username.
     * @param password the database password.
     */
    public ConnectionPool(String jdbcDriver, String url, String username, String password) {
        try {
            // Loading the JDBC driver
            Class.forName(jdbcDriver);

            // Creating a pool of connections
            pool = new GenericObjectPool(null);

            // Setting min/max number of idle/active connections
            pool.setMinIdle(10);
            pool.setMaxActive(20);

            // Creating a poolable data source
            ConnectionFactory cf = new DriverManagerConnectionFactory(url, username, password);

            PoolableConnectionFactory pcf = new PoolableConnectionFactory(cf, pool, null, null, false, true);

            datasource = new PoolingDataSource(pool);
            
            logger.info("Connection manager loaded successfully linked to database '" + url + "'.");
        } catch (ClassNotFoundException exc) {
            logger.error("An error occurred loading the JDBC driver: '" + exc.getMessage() + "'.");
        } catch (Exception exc) {
            logger.error("An unknown error occurred setting up a connection manager: '" + exc.getMessage() + "'.");
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
            logger.error("An unknown error occurred getting a new connection session: '" + exc.getMessage() + "'.");
        }

        return session;
    }

    /**
     * A method resetting the connection manager.
     */
    @Override
    public void reset() {
        if (pool != null && !pool.isClosed()) {
            pool.clear();
        }
    }
    
    /**
     * A method closing the connection manager.
     */
    @Override
    public void close() {
        if (pool != null && !pool.isClosed()) {
            try {
                pool.close();
            } catch (Exception exc) {
                logger.error("An unknown error occurred closing connection manager: '" + exc.getMessage() + "'.");
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("[ConnectionManager ");

        if (pool != null) {
            sb.append("NIC: '").append(pool.getNumIdle()).append("', ")
              .append("NAC: '").append(pool.getNumActive()).append("'");
        } else {
            sb.append(" 'null'");
        }

        sb.append("]");

        return sb.toString();
    }

    @Override
    protected void finalize() {
        try {
            super.finalize();
        } catch (Throwable exc) {
            logger.error("An unknown error occurred finalizing the connection manager: '" + exc.getMessage() + "'.");
        }
    }
}
