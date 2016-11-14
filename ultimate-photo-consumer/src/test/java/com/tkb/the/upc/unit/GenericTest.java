package com.tkb.the.upc.unit;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import com.tkb.the.upc.service.FlickerPhotoSearchService;
import com.tkb.the.upc.model.OrderMode;
import com.tkb.the.upc.model.PhotoItem;
import com.tkb.the.upc.model.PhotoItemPage;
import com.tkb.the.upc.service.FivepixPhotoSearchService;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.apache.log4j.Logger;

/**
 * A generic test case.
 *
 * @author Akis Papadopoulos
 */
public class GenericTest {

    private static final Logger logger = Logger.getLogger(GenericTest.class);

    private static final String flickrApiKey = "1a6b6b1013dbb8c633c3135103c4ad78";
    
    private static final String pxConsumerKey = "d7NkZlpxGAO1VHlhsJGhC2V8qYT7JPkKpPh5w5IO";

    @Test
    public void testFlickrPhotosSearchService() throws Exception {
        FlickerPhotoSearchService endpoint = new FlickerPhotoSearchService(flickrApiKey, 60, OrderMode.MOST_RATED);

        String result = endpoint.search("christo redentor rio", null, 1);

        PhotoItemPage page = endpoint.extract(result);

        List<PhotoItem> photos = page.getPhotos();
        
        logger.info("P: " + result.substring(0, 80));

        assertTrue(photos != null && !photos.isEmpty());

        // TMP
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter("/tmp/flickr-result.html"));
            StringBuilder context = new StringBuilder();

            context.append("<!DOCTYPE html><html><head>Flickr Results</head><body><ol>");

            for (PhotoItem photo : page.getPhotos()) {
                context.append("<li>")
                        .append("<a href=\"" + photo.getPhotoUrl() + "\" target=\"_blank\">")
                        .append("<img src=\"" + photo.getThumbnailUrl() + "\">")
                        .append("</a></li>");
            }

            context.append("</ol></body></html>");

            writer.write(context.toString());
        } catch (IOException exc) {
            throw exc;
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
        // TMP
    }

    @Test
    public void test500pxPhotoSearchService() throws Exception {
        FivepixPhotoSearchService endpoint = new FivepixPhotoSearchService(pxConsumerKey, 60, OrderMode.MOST_RELEVANT);

        String result = endpoint.search("sydney opera", null, 1);

        PhotoItemPage page = endpoint.extract(result);

        List<PhotoItem> photos = page.getPhotos();
        
        logger.info("P: " + result.substring(0, 80));

        assertTrue(photos != null && !photos.isEmpty());

        // TMP
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter("/tmp/500px-result.html"));
            StringBuilder context = new StringBuilder();

            context.append("<!DOCTYPE html><html><head>500px Results</head><body><ol>");

            for (PhotoItem photo : page.getPhotos()) {
                context.append("<li>")
                        .append("<a href=\"" + photo.getPhotoUrl() + "\" target=\"_blank\">")
                        .append("<img src=\"" + photo.getThumbnailUrl() + "\">")
                        .append("</a></li>");
            }

            context.append("</ol></body></html>");

            writer.write(context.toString());
        } catch (IOException exc) {
            throw exc;
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
        // TMP
    }
}
