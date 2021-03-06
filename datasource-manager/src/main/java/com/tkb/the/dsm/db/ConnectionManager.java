package com.tkb.the.dsm.db;

/**
 * An abstract data source connection manager.
 *
 * @author Akis Papadopoulos, iakopap@gmail.com
 */
public interface ConnectionManager {

    public void setMinIdle(int minIdle);
    
    public void setMaxIdle(int maxIdle);

    public void setMaxActive(int maxActive);

    public ConnectionSession getSession();

    public void reset();
    
    public void close();
}
