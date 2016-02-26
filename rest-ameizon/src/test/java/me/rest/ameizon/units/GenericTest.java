package me.rest.ameizon.units;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < 10000; i++) {
            sb.append("abcdefghijklmnopqrstuvwxyz\n");
        }
        
        InputStream inputStream = new ByteArrayInputStream(sb.toString().getBytes());

        // TMP
        long s = System.currentTimeMillis();

        AmazonStorageService service;
        service = new AmazonStorageService("media.tz3ik.shutter",
                "EU_CENTRAL_1", "uploads",
                "***", "***");

        // TMP
        System.out.println("Tm: " + (System.currentTimeMillis() - s) / 1000.0);

        //File file = new File("/home/bob/Downloads/testing/1m.jpg");

        // TMP
        s = System.currentTimeMillis();

        service.put(inputStream, 131072, "temp.txt", "text/plain");

        // TMP
        System.out.println("Tm: " + (System.currentTimeMillis() - s) / 1000.0);
    }
}
