package me.restables.impl;

import me.restables.utils.model.PhotoItemPage;

/**
 * An abstract implementation in order to extract a result page of photo items
 * from response in raw JSON format.
 *
 * @author Akis Papadopoulos
 */
public interface PhotoItemExtractor {

    /**
     * A method extracting a result page of photo items given the JSON response
     * in raw text form.
     *
     * @param json the JSON response in plain text raw format.
     * @return a result page containing the list of photo items.
     * @throws Exception throws unknown error exceptions.
     */
    public PhotoItemPage extract(String json) throws Exception;
}
