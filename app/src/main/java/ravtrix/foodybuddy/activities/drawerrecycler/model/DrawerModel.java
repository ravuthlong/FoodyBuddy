package ravtrix.foodybuddy.activities.drawerrecycler.model;

/**
 * Created by Ravinder on 1/30/17.
 */

public class DrawerModel {

    private String restaurantName, time, address;

    public DrawerModel(String restaurantName, String time, String address) {
        this.restaurantName = restaurantName;
        this.time = time;
        this.address = address;
    }

    public String getRestaurantName() {
        return restaurantName;
    }
    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
