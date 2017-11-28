package ravtrix.foodybuddy.network.networkmodel;

/**
 * Created by rlong on 11/7/17.
 */

public class NearbyEventsParam {

    private String lat;
    private String lng;
    private String distance;

    public NearbyEventsParam(String lat, String lng, String distance) {
        this.lat = lat;
        this.lng = lng;
        this.distance = distance;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
