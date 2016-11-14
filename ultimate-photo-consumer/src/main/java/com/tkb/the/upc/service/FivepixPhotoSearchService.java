package com.tkb.the.upc.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kevinsawicki.http.HttpRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.tkb.the.upc.impl.PhotoItemExtractor;
import com.tkb.the.upc.model.InterestPoint;
import com.tkb.the.upc.model.OrderMode;
import com.tkb.the.upc.model.PhotoItem;
import com.tkb.the.upc.model.PhotoItemPage;

/**
 * A photo web service consumer implementation using the 500px search photo API
 * fetching photo items regarding both textual and geospatial parameters.
 *
 * @author Akis papadopoulos
 */
public class FivepixPhotoSearchService implements PhotoItemExtractor {
    
    // Thumbnail size identifier regarding 500px docs
    private static final String SIZE_ID = "21";
    
    // Thumbnail size in the longest edge in pixels
    private static final int SIZE_ON_LONGEST_EDGE = 600;

    // Service URL
    private static final String SERVICE_URL = "https://api.500px.com/v1/photos/search?";

    // Service consumer key
    private String consumerKey;

    // Number of items per page
    private int pageSize;

    // Ordering mode
    private OrderMode order;

    /**
     * A constructor initiating a 500px photo consumer given the service
     * credentials.
     *
     * @param consumerKey the consumer key.
     * @param pageSize the number of items per page.
     * @param order how the photo items should sorted.
     */
    public FivepixPhotoSearchService(String consumerKey, int pageSize, OrderMode order) {
        this.consumerKey = consumerKey;
        this.pageSize = pageSize;
        this.order = order;
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

        // Setting image size to 900px on the longest edge
        params.put("image_size", SIZE_ID);
        
        // Setting sort order mode
        if (order == OrderMode.MOST_RECENT) {
            params.put("sort", "created_at");
        } else if(order == OrderMode.MOST_RELEVANT) {
            params.put("sort", "_score");
        } else if(order == OrderMode.MOST_RATED) {
            params.put("sort", "times_viewed");
        }
        
        params.put("rpp", String.valueOf(pageSize));
        params.put("page", String.valueOf(page));

        // Sending the request
        result = HttpRequest.get(SERVICE_URL, params, true).accept("application/json").body();

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
            photo.setLikes(item.path("votes_count").asInt());
            photo.setViews(item.path("times_viewed").asInt());

            photo.setLatitude(item.path("latitude").asDouble());
            photo.setLongitude(item.path("longitude").asDouble());

            // Expecting only one image thumbnail with 256px on the longest edge
            JsonNode imagesNode = item.path("images").elements().next();
            String imageURL = imagesNode.path("url").asText();
            photo.setThumbnailUrl(imageURL);

            // Calculating the thumbnail dims regarding the original aspect ratio
            int oW = item.path("width").asInt();
            int oH = item.path("height").asInt();
            
            double ratio = (double) oW / (double) oH;

            int tW, tH;

            if (oW > oH) {
                tW = SIZE_ON_LONGEST_EDGE;
                tH = (int) Math.round(tW / ratio);
            } else if (oH > oW) {
                tH = SIZE_ON_LONGEST_EDGE;
                tW = (int) Math.round(tH * ratio);
            } else {
                tW = tH = SIZE_ON_LONGEST_EDGE;
            }

            photo.setThumbnailWidth(tW);
            photo.setThumbnailHeight(tH);

            photo.setPhotoUrl("https://500px.com" + item.path("url").asText());

            JsonNode userNode = item.path("user");
            photo.setOwner(userNode.path("fullname").asText());
            photo.setProfileUrl("https://500px.com/" + userNode.path("username").asText());

            photo.setOrigin("500px");

            photos.add(photo);
        }

        return new PhotoItemPage(page, pages, count, photos, order);
    }
}
