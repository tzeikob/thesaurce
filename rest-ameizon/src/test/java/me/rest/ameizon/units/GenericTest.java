package me.rest.ameizon.units;

import java.io.File;
import me.rest.ameizon.endpoints.AmazonStorageService;
import org.junit.Test;
import org.apache.log4j.Logger;

/**
 * A generic test case.
 *
 * @author Akis Papadopoulos
 */
public class GenericTest {

    private static final Logger logger = Logger.getLogger(GenericTest.class);

    @Test
    public void testStorageService() throws Exception {
        AmazonStorageService service = new AmazonStorageService("media.tz3ik.shutter", "EU_CENTRAL_1", "uploads", "", "");
        
        File file = new File("/home/bob/Downloads/1m.jpg");
        
        service.put(file);
    }
}
