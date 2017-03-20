package ravtrix.foodybuddy.fragments.deals.recyclerview.model;

/**
 * Created by Emily on 3/9/17.
 */

public class DealsModel {

    private String restaurantName, dealTitle, dealDescription, address, url, imageURL;

    public DealsModel(String restaurantName, String dealDescription, String address, String url,
                      String imageURL, String dealTitle) {
        this.restaurantName = restaurantName;
        this.dealDescription = dealDescription;
        this.address = address;
        this.url = url;
        this.imageURL = imageURL;
        this.dealTitle = dealTitle;
    }

    public String getDealTitle() {
        return dealTitle;
    }

    public void setDealTitle(String dealTitle) {
        this.dealTitle = dealTitle;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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