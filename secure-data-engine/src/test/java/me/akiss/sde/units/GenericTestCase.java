package me.akiss.sde.units;

import java.util.HashSet;
import java.util.Set;
import me.akiss.sde.salters.Salter;
import me.akiss.sde.salters.SecureRandomSalter;
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
    
    @Test
    public void testSpill() {
        Salter salter = new SecureRandomSalter();
        
        Set<String> salts = new HashSet<>();
        
        for(int i = 0; i < 10000; i++) {
            salts.add(salter.spill());
        }
        
        assertTrue(salts.size() == 10000);
    }
}
