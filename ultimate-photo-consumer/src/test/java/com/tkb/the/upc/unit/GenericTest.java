package com.tkb.the.upc.unit;

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

    private static final String flickrApiKey = "PASTE_FLICKR_API_KEY";
    
    private static final String pxConsumerKey = "PASTE_500PX_CONSUMER_KEY";

    @Test
    public void testFlickrPhotosSearchService() throws Exception {
        FlickerPhotoSearchService endpoint = new FlickerPhotoSearchService(flickrApiKey, 60, OrderMode.MOST_RATED);

        String result = endpoint.search("christo redentor rio", null, 1);

        PhotoItemPage page = endpoint.extract(result);

        List<PhotoItem> photos = page.getPhotos();

        assertTrue("Please set a valid Flickr API KEY before testing.", photos != null && !photos.isEmpty());
    }

    @Test
    public void test500pxPhotoSearchService() throws Exception {
        FivepixPhotoSearchService endpoint = new FivepixPhotoSearchService(pxConsumerKey, 60, OrderMode.MOST_RELEVANT);

        String result = endpoint.search("sydney opera", null, 1);

        PhotoItemPage page = endpoint.extract(result);

        List<PhotoItem> photos = page.getPhotos();

        assertTrue("\"Please set a valid 500px CONSUMER KEY before testing.\",", photos != null && !photos.isEmpty());
    }
}
