package me.restables.utils.model;

/**
 * A photo item encapsulating various textual metadata.
 *
 * @author Akis Papadopoulos
 */
public class PhotoItem {

    // Origin wised identifier
    private String id;

    // Owner name
    private String owner;

    // Title
    private String title;

    // Number of hits, likes or views
    private int hits;

    // Latitude cords
    private double latitude;

    // Longitude cords
    private double longitude;

    // Thumbnail URL path
    private String thumbnailUrl;

    // Thumbnail width
    private int width;

    // Thumbnail height
    private int height;

    // Photo URL path
    private String photoUrl;
    
    // Owner's profile URL path
    private String profileUrl;

    // Photo service provider
    private String provider;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
