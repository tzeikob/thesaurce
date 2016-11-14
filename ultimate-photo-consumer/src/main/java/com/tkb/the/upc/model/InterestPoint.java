package com.tkb.the.upc.model;

/**
 * A generic geo-spatial point encapsulating both the latitude and longitude
 * cords of an interested location setting optionally the bounding area given a
 * radius.
 *
 * @author Akis Papadopoulos
 */
public class InterestPoint {

    // Latitude coordinates
    private double latitude;

    // Longitude coordinates
    private double longitude;

    // Bounding area radius in Km
    private double radius;

    /**
     * A constructor initiating a default point of interest.
     */
    public InterestPoint() {
        latitude = 0.0;
        longitude = 0.0;
        radius = 1.0;
    }

    /**
     * A constructor initiating a point of interest given the geo-spatial
     * coordinates and the bounding area.
     *
     * @param latitude the latitude coordinates.
     * @param longitude the longitude coordinates.
     * @param radius the bounding area radius.
     */
    public InterestPoint(double latitude, double longitude, double radius) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
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

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
