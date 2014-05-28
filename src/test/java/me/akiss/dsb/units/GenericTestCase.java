package me.akiss.dsb.units;

import java.util.ArrayList;
import java.util.List;
import me.akiss.dsb.db.ConnectionManager;
import me.akiss.dsb.db.ConnectionPool;
import me.akiss.dsb.db.ConnectionSession;
import org.apache.log4j.Logger;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * A generic unit test case implementation.
 * 
 * @author Akis Papadopoulos, iakopap@gmail.com
 */
public class GenericTestCase {

    // Logger
    private static final Logger logger = Logger.getLogger(GenericTestCase.class);
    
    // Connection manager
    private static ConnectionManager cm = new ConnectionPool("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/lf62_plain?useUnicode=true&characterEncoding=UTF-8&useFastDateParsing=false", "root", "root");
    
    @Test
    public void testConnection() {
        ConnectionSession cs = cm.getSession();
        
        assertNotNull(cs);
    }
    
    @Test
    public void testMultipleConnections() {
        List<ConnectionSession> sessions = new ArrayList<ConnectionSession>();
        
        cm.setMinIdleConnections(10);
        cm.setMaxActiveConnections(31);
        
        for (int i = 0; i < 30; i++) {
            ConnectionSession session = cm.getSession();
            
            sessions.add(session);
        }
        
        assertTrue(sessions.size() == 30);
    }
}
