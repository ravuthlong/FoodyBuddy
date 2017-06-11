package ravtrix.foodybuddy.network.networkmodel;

/**
 * Created by Ravinder on 6/6/17.
 */

public class UserLocationParam {
    private int user_id;
    private double lng;
    private double lat;

    public UserLocationParam(int userID, double lat, double lng) {
        this.user_id = userID;
        this.lat = lat;
        this.lng = lng;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(long lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }
}
