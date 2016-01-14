package me.restables.utils.model;

import java.util.List;

/**
 * An generic result page encapsulating both the page related information and
 * the photo item list.
 *
 * @author Akis Papadopoulos
 */
public class PhotoItemPage {

    // Page serial number
    private int page;

    // Number of total pages
    private int pages;

    // Total number of items
    private int total;

    // Photo item list
    private List<PhotoItem> photos;

    /**
     * A constructor initiates a result page given both page information and the
     * list of the photo items.
     *
     * @param page the serial number of the page.
     * @param pages the total number of the pages.
     * @param total the total number of items.
     * @param photos the list of photo items.
     */
    public PhotoItemPage(int page, int pages, int total, List<PhotoItem> photos) {
        this.page = page;
        this.pages = pages;
        this.total = total;
        this.photos = photos;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<PhotoItem> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoItem> photos) {
        this.photos = photos;
    }
}
