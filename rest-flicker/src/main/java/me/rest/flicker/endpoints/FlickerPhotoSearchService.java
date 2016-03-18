package me.rest.flicker.endpoints;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kevinsawicki.http.HttpRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import me.rest.utils.impl.PhotoItemExtractor;
import me.rest.utils.model.InterestPoint;
import me.rest.utils.model.OrderMode;
import me.rest.utils.model.PhotoItem;
import me.rest.utils.model.PhotoItemPage;

/**
 * A photo web service consumer implementation using the Flickr search photo API
 * fetching photo items regarding both textual and geospatial parameters.
 *
 * @author Akis papadopoulos
 */
public class FlickerPhotoSearchService implements PhotoItemExtractor {

    // Image size suffix letters regarding flickr's documentation
    private static final String[] suffixes = {"z", "n", "m"};

    // Service URL
    private String serviceURL;

    // Service API key
    private String apiKey;

    // Number of items per page
    private int pageSize;

    // Ordering mode
    private OrderMode order;

    /**
     * A constructor initiating a Flickr photo consumer given the service
     * credentials.
     *
     * @param serviceURL the service URL.
     * @param apiKey the API token.
     * @param pageSize the number of items per page.
     * @param order how the photo items should sorted.
     */
    public FlickerPhotoSearchService(String serviceURL, String apiKey, int pageSize, OrderMode order) {
        this.serviceURL = serviceURL;
        this.apiKey = apiKey;
        this.pageSize = pageSize;
        this.order = order;
    }

    /**
     * A method sending a search request given various query parameters.
     *
     * @param text the optional text to search about.
     * @param location the geo-spatial point of interest.
     * @param page the number of result page.
     * @return a JSON raw text of the response.
     * @throws Exception throws unknown error exceptions.
     */
    public String search(String text, InterestPoint location, int page) throws Exception {
        String result = null;

        // Adding the request parameters
        Map<String, String> params = new HashMap<String, String>();
        params.put("method", "flickr.photos.search");
        params.put("api_key", apiKey);
        params.put("format", "json");
        params.put("nojsoncallback", "1");
        params.put("media", "photos");

        // Settign optional text parameter
        if (text != null) {
            params.put("text", text);
        }

        // Setting optional geo-spatial parameter
        if (location != null) {
            params.put("has_geo", "1");
            params.put("lat", String.valueOf(location.getLatitude()));
            params.put("lon", String.valueOf(location.getLongitude()));
            params.put("radius", String.valueOf(location.getRadius()));
            params.put("radius_units", "km");
        }
        
        // Setting sort order mode
        if (order == OrderMode.MOST_RECENT) {
            params.put("sort", "date-posted-desc");
        } else if(order == OrderMode.MOST_RELEVANT) {
            params.put("sort", "relevance");
        } else if(order == OrderMode.MOST_RATED) {
            params.put("sort", "interestingness-desc");
        }

        params.put("safe_search", "1");

        // Requesting some extra metadata
        StringBuilder extras = new StringBuilder();
        extras.append("owner_name,geo,count_faves,views");

        // Adding size aware parameters regarding flickr wise letter suffixes
        for (int i = 0; i < suffixes.length; i++) {
            extras.append(",url_").append(suffixes[i]);
        }

        params.put("extras", extras.toString());

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
            photo.setLikes(item.path("count_faves").asInt());
            photo.setViews(item.path("views").asInt());

            photo.setLatitude(item.path("latitude").asDouble());
            photo.setLongitude(item.path("longitude").asDouble());

            // Setting the first matched thumbnail otherwise null
            String thumbnailUrl;

            for (int i = 0; i < suffixes.length; i++) {
                thumbnailUrl = item.path("url_" + suffixes[i]).asText();

                if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
                    // Setting the metadata and then break the loop
                    photo.setThumbnailUrl(thumbnailUrl);

                    int width = item.path("width_" + suffixes[i]).asInt();
                    int height = item.path("height_" + suffixes[i]).asInt();

                    photo.setThumbnailWidth(width);
                    photo.setThumbnailHeight(height);

                    break;
                }
            }

            String userId = item.path("owner").asText();
            photo.setPhotoUrl("https://www.flickr.com/photos/" + userId + "/" + photoId);
            photo.setProfileUrl("https://www.flickr.com/photos/" + userId);

            photo.setOrigin("flickr");

            photos.add(photo);
        }

        return new PhotoItemPage(page, pages, count, photos, order);
    }
}
