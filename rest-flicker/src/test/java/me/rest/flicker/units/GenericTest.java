package me.rest.flicker.units;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import me.rest.flicker.endpoints.FlickerPhotoSearchService;
import me.rest.utils.model.InterestPoint;
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

    private static final String serviceURL = "https://api.flickr.com/services/rest/?";

    private static final String apiKey = "1a6b6b1013dbb8c633c3135103c4ad78";

    @Test
    public void testPhotosSearchService() throws Exception {
        FlickerPhotoSearchService endpoint = new FlickerPhotoSearchService(serviceURL, apiKey, 30, false);

        String result = endpoint.search("athens acropolis parthenon", new InterestPoint(37.9780914, 23.7368875, 0.3), 1);

        PhotoItemPage page = endpoint.extract(result);

        List<PhotoItem> photos = page.getPhotos();

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
}
