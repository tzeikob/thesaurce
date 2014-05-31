package me.akiss.dbb.db;

/**
 * An abstract data source connection manager.
 *
 * @author Akis Papadopoulos, iakopap@gmail.com
 */
public interface ConnectionManager {

    public void setMinIdleConnections(int minIdleConnections);

    public void setMaxActiveConnections(int maxActiveConnections);

    public ConnectionSession getSession();

    public void reset();
    
    public void close();
}
