package ravtrix.foodybuddy.fragments.maineventfrag.recyclerview.model;

/**
 * Created by Ravinder on 1/28/17.
 */

public class EventModel {

    private String profileImage;
    private String restaurantName;
    private String postTime;
    private String eventTime;
    private String eventDescription;
    private String address;
    private String distance;
    private int numComment;

    public EventModel(String profileImage, String retaurantName, String postTime,
                      String eventTime, String eventDescription, String address, String distance, int numComment) {
        this.profileImage = profileImage;
        this.restaurantName = retaurantName;
        this.postTime = postTime;
        this.eventTime = eventTime;
        this.eventDescription = eventDescription;
        this.address = address;
        this.distance = distance;
        this.numComment = numComment;
    }
    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getNumComment() {
        return numComment;
    }

    public void setNumComment(int numComment) {
        this.numComment = numComment;
    }
}
