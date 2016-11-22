package com.tkb.the.asu.unit;

import com.tkb.the.asu.service.AmazonStorageService;
import java.io.File;
import org.junit.Test;
import org.apache.log4j.Logger;
import static org.junit.Assert.assertNotNull;

/**
 * A generic test case.
 *
 * @author Akis Papadopoulos
 */
public class GenericTest {

    private static final Logger logger = Logger.getLogger(GenericTest.class);

    @Test
    public void testStorageService() throws Exception {
        AmazonStorageService ass = new AmazonStorageService("bucket", "EU_WEST_1", "uploads", "accessKey", "secretKey");

        File file = new File("/path/to/file");

        String md5 = ass.put(file, true);

        assertNotNull(md5);
    }
}
