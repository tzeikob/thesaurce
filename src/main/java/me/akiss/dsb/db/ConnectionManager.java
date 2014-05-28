package me.akiss.dsb.db;

/**
 * A poolable connection manager.
 *
 * @author Akis Papadopoulos, iakopap@gmail.com
 */
public interface ConnectionManager {

    public ConnectionSession getSession();
    public void reset();
}
