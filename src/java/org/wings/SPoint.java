package org.wings;

/**
 * @author hengels
 * @version $Revision$
 */
public class SPoint {
    private String coordinates;

    public SPoint(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String toString() {
        return coordinates;
    }
}
