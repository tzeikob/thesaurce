package me.akiss.dsb.units;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private static ConnectionManager cm = new ConnectionPool("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/agrodb?useUnicode=true&characterEncoding=UTF-8&useFastDateParsing=false", "root", "root");
    
    @Test
    public void testConnection() {
        ConnectionSession cs = cm.getSession();
        
        assertNotNull(cs);
    }
    
    @Test
    public void testMultipleConnections() {
        List<ConnectionSession> sessions = new ArrayList<ConnectionSession>();
        
        cm.setMinIdleConnections(10);
        cm.setMaxActiveConnections(21);
        
        for (int i = 0; i < 20; i++) {
            ConnectionSession session = cm.getSession();
            
            sessions.add(session);
        }
        
        assertTrue(sessions.size() == 20);
    }
    
    @Test
    public void testSelectStatement() throws SQLException {
        String query = "SELECT * FROM fields WHERE region LIKE ?";
        
        ConnectionSession cs = cm.getSession();
        
        PreparedStatement ps = cs.getStatement(query);
        ps.setString(1, "ΜΠΑ%");
        ResultSet result = ps.executeQuery();
        
        while(result.next()) {
            logger.info("[" + result.getString("fid") + ", " + result.getString("region") + "]");
        }
        
        cs.close(ps, result);
        
        assertTrue(cs.isClosed());
    }
}
