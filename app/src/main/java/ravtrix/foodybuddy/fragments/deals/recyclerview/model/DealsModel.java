package ravtrix.foodybuddy.fragments.deals.recyclerview.model;

/**
 * Created by Emily on 3/9/17.
 */

public class DealsModel {

    private String restaurantName, dealDescription, address, url;

    public DealsModel(String restaurantName, String dealDescription, String address, String url) {
        this.restaurantName = restaurantName;
        this.dealDescription = dealDescription;
        this.address = address;
        this.url = url;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getDealDescription() {
        return dealDescription;
    }

    public String getAddress() {
        return address;
    }

    public String getUrl() {
        return url;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public void setDealDescription(String dealDescription) {
        this.dealDescription = dealDescription;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
