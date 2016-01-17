package me.rest.fivepix.units;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import me.rest.fivepix.endpoints.Five00pxPhotoSearchService;
import me.rest.utils.model.PhotoItem;
import me.rest.utils.model.PhotoItemPage;
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

    private static final String serviceURL = "https://api.500px.com";

    private static final String consumerKey = "d7NkZlpxGAO1VHlhsJGhC2V8qYT7JPkKpPh5w5IO";

    @Test
    public void testPhotoSearchService() throws Exception {
        Five00pxPhotoSearchService endpoint = new Five00pxPhotoSearchService(serviceURL, consumerKey, 30, false);

        String result = endpoint.search("athens acropolis parthenon", 37.9780914, 23.7368875, 0.3, 1);

        PhotoItemPage page = endpoint.extract(result);

        List<PhotoItem> photos = page.getPhotos();

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
