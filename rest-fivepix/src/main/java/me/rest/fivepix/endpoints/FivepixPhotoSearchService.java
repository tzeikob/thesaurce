package me.rest.fivepix.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kevinsawicki.http.HttpRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import me.rest.utils.impl.PhotoItemExtractor;
import me.rest.utils.model.InterestPoint;
import me.rest.utils.model.PhotoItem;
import me.rest.utils.model.PhotoItemPage;

/**
 * A photo web service consumer implementation using the 500px search photo API
 * fetching photo items regarding both textual and geospatial parameters.
 *
 * @author Akis papadopoulos
 */
public class FivepixPhotoSearchService implements PhotoItemExtractor {

    // Service URL
    private String serviceURL;

    // Service consumer key
    private String consumerKey;

    // Number of items per page
    protected int pageSize = 20;

    // Option to shuffle the results
    protected boolean shuffle = false;

    /**
     * A constructor initiating a 500px photo consumer given the service
     * credentials.
     *
     * @param serviceURL the service URL.
     * @param consumerKey the consumer key.
     * @param pageSize the number of items per page.
     * @param shuffle true to shuffle the items order otherwise false.
     */
    public FivepixPhotoSearchService(String serviceURL, String consumerKey, int pageSize, boolean shuffle) {
        this.serviceURL = serviceURL;
        this.consumerKey = consumerKey;
        this.pageSize = pageSize;
        this.shuffle = shuffle;
    }

    /**
     * A method sending a search request given various query parameters.
     *
     * @param text the optional text to search about.
     * @param page the number of result page.
     * @return a JSON raw text of the response.
     * @throws Exception throws unknown error exceptions.
     */
    public String search(String text, InterestPoint location, int page) throws Exception {
        String result = null;

        // Adding the request parameters
        Map<String, String> params = new HashMap<String, String>();
        params.put("consumer_key", consumerKey);

        // Setting optional text parameter
        if (text != null) {
            params.put("term", text);
        }

        // Setting optional geo-spatial parameters
        if (location != null) {
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            double radius = location.getRadius();
            
            params.put("geo", lat + "," + lon + "," + radius + "km");
        }

        params.put("image_size", "30,200");
        params.put("sort", "_score");
        params.put("rpp", String.valueOf(pageSize));
        params.put("page", String.valueOf(page));

        // Sending the request
        result = HttpRequest.get(serviceURL + "/v1/photos/search?", params, true).accept("application/json").body();

        return result;
    }

    /**
     * A method extracting a result page of photo items given the JSON response
     * in raw text form.
     *
     * @param json the JSON response in plain text raw format.
     * @return a result page containing the list of photo items.
     * @throws Exception throws unknown error exceptions.
     */
    @Override
    public PhotoItemPage extract(String json) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(json);

        // Extracting page related information
        int page = rootNode.path("current_page").asInt();
        int pages = rootNode.path("total_pages").asInt();
        int count = rootNode.path("total_items").asInt();

        // Extracting the photo items
        JsonNode photosNode = rootNode.path("photos");
        Iterator<JsonNode> items = photosNode.elements();

        List<PhotoItem> photos = new ArrayList<PhotoItem>();

        while (items.hasNext()) {
            JsonNode item = items.next();

            PhotoItem photo = new PhotoItem();

            photo.setId(item.path("id").asText());
            photo.setTitle(item.path("name").asText());
            photo.setHits(item.path("votes_count").asInt());

            photo.setLatitude(item.path("latitude").asDouble());
            photo.setLongitude(item.path("longitude").asDouble());

            String imageURL = item.path("image_url").asText();
            photo.setThumbnailUrl(imageURL);

            photo.setPhotoUrl("https://500px.com" + item.path("url").asText());

            JsonNode userNode = item.path("user");
            photo.setOwner(userNode.path("fullname").asText());
            photo.setProfileUrl("https://500px.com/" + userNode.path("username").asText());
            
            photo.setOrigin("500px");

            photos.add(photo);
        }

        // Shuffling items randomly
        if (shuffle) {
            Collections.shuffle(photos, new Random());
        }

        return new PhotoItemPage(page, pages, count, photos);
    }
}
