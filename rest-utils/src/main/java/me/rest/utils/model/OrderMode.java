package me.rest.utils.model;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * An enumeration of various order modes.
 * 
 * @author Akis Papadopoulos
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OrderMode {

    MOST_RECENT(0, "Most recent items first in terms of upload time."),
    MOST_RELEVANT(1, "Most relevant items first in terms of relevance."),
    MOST_RATED(2, "Most rated items first in terms of popularity.");

    private int code;

    private String message;

    private OrderMode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
