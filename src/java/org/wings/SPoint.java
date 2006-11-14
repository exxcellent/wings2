package org.wings;

/**
 * @author hengels
 */
public class SPoint {
    private String coordinates;

    public SPoint(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getCoordinates() {
        return coordinates;
    }

    protected void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String toString() {
        return coordinates;
    }
}
