package me.rest.flicker.endpoints;

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
import me.rest.utils.model.PhotoItem;
import me.rest.utils.model.PhotoItemPage;

/**
 * A photo web service consumer implementation using the Flickr search photo API
 * fetching photo items regarding both textual and geospatial parameters.
 *
 * @author Akis papadopoulos
 */
public class FlickrPhotoSearchService implements PhotoItemExtractor {

    // Service URL
    private String serviceURL;

    // Service API key
    private String apiKey;

    // Number of items per page
    protected int pageSize = 20;

    // Option to shuffle the results
    protected boolean shuffle = false;

    /**
     * A constructor initiating a Flickr photo consumer given the service
     * credentials.
     *
     * @param serviceURL the service URL.
     * @param apiKey the API token.
     * @param pageSize the number of items per page.
     * @param shuffle true to shuffle the items order otherwise false.
     */
    public FlickrPhotoSearchService(String serviceURL, String apiKey, int pageSize, boolean shuffle) {
        this.serviceURL = serviceURL;
        this.apiKey = apiKey;
        this.pageSize = pageSize;
        this.shuffle = shuffle;
    }

    /**
     * A method sending a search request given various query parameters.
     *
     * @param text the optional text to search about.
     * @param latitude the optional latitude cords of the point of interest.
     * @param longitude the optional longitude cords of the point of interest.
     * @param radius the optional radius around the point of interest in km.
     * @param page the number of result page.
     * @return a JSON raw text of the response.
     * @throws Exception throws unknown error exceptions.
     */
    public String search(String text, Double latitude, Double longitude, Double radius, int page) throws Exception {
        String result = null;

        // Adding the request parameters
        Map<String, String> params = new HashMap<String, String>();
        params.put("method", "flickr.photos.search");
        params.put("api_key", apiKey);
        params.put("format", "json");
        params.put("nojsoncallback", "1");
        params.put("media", "photos");

        // Settign optional text
        if (text != null) {
            params.put("text", text);
        }

        // Setting optional geospatial
        if (latitude != null && longitude != null) {
            params.put("has_geo", "1");
            params.put("lat", String.valueOf(latitude));
            params.put("lon", String.valueOf(longitude));

            if (radius != null) {
                params.put("radius", String.valueOf(radius));
            }
        }

        params.put("sort", "relevance");
        params.put("safe_search", "1");
        params.put("extras", "owner_name,url_q,url_m,url_n,geo,count_faves");
        params.put("per_page", String.valueOf(pageSize));
        params.put("page", String.valueOf(page));

        // Sending the request
        result = HttpRequest.get(serviceURL, params, true).accept("application/json").body();

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
        JsonNode photosNode = rootNode.path("photos");

        // Extracting page related information
        int page = photosNode.path("page").asInt();
        int pages = photosNode.path("pages").asInt();
        int count = photosNode.path("total").asInt();

        // Extracting the photo items
        JsonNode photoNode = photosNode.path("photo");
        Iterator<JsonNode> items = photoNode.elements();

        List<PhotoItem> photos = new ArrayList<PhotoItem>();

        while (items.hasNext()) {
            JsonNode item = items.next();

            PhotoItem photo = new PhotoItem();

            String photoId = item.path("id").asText();
            photo.setId(photoId);

            photo.setOwner(item.path("ownername").asText());
            photo.setTitle(item.path("title").asText());
            photo.setHits(item.path("count_faves").asInt());

            photo.setLatitude(item.path("latitude").asDouble());
            photo.setLongitude(item.path("longitude").asDouble());

            // Setting the first available URL otherwise null or empty
            String thumbnailUrl = item.path("url_n").asText();
            int width = item.path("width_n").asInt();
            int height = item.path("height_n").asInt();

            if (thumbnailUrl == null || thumbnailUrl.isEmpty()) {
                thumbnailUrl = item.path("url_m").asText();
                width = item.path("width_m").asInt();
                height = item.path("height_m").asInt();

                if (thumbnailUrl == null || thumbnailUrl.isEmpty()) {
                    thumbnailUrl = item.path("url_q").asText();
                    width = item.path("width_q").asInt();
                    height = item.path("height_q").asInt();
                }
            }

            photo.setThumbnailUrl(thumbnailUrl);
            photo.setWidth(width);
            photo.setHeight(height);

            String userId = item.path("owner").asText();
            photo.setPhotoUrl("https://www.flickr.com/photos/" + userId + "/" + photoId);
            photo.setProfileUrl("https://www.flickr.com/photos/" + userId);

            photos.add(photo);
        }

        // Shuffling items randomly
        if (shuffle) {
            Collections.shuffle(photos, new Random());
        }

        return new PhotoItemPage(page, pages, count, photos, "flickr");
    }
}
