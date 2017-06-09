package ravtrix.foodybuddy.fragments.maineventfrag.recyclerview.model;

/**
 * Created by Ravinder on 1/28/17.
 */

public class EventModel {

    private String profileImage, restaurantName, postTime, eventTime, eventDescription, address, distance,
                    userImage1, userImage2, userImage3, userImage4, restaurantImage;
    private int numComment;

    public EventModel(String profileImage, String retaurantName, String postTime,
                      String eventTime, String eventDescription, String address, String distance, int numComment,
                      String userImage1, String userImage2, String userImage3, String userImage4, String restaurantImage) {
        this.profileImage = profileImage;
        this.restaurantName = retaurantName;
        this.postTime = postTime;
        this.eventTime = eventTime;
        this.eventDescription = eventDescription;
        this.address = address;
        this.distance = distance;
        this.numComment = numComment;
        this.userImage1 = userImage1;
        this.userImage2 = userImage2;
        this.userImage3 = userImage3;
        this.userImage4 = userImage4;
        this.restaurantImage = restaurantImage;
    }

    public String getUserImage1() {
        return userImage1;
    }

    public void setUserImage1(String userImage1) {
        this.userImage1 = userImage1;
    }

    public String getrestaurantImage() {
        return restaurantImage;
    }

    public void serestaurantImage(String restaurantImage) {
        this.restaurantImage = restaurantImage;
    }

    public String getUserImage2() {
        return userImage2;
    }

    public void setUserImage2(String userImage2) {
        this.userImage2 = userImage2;
    }

    public String getUserImage3() {
        return userImage3;
    }

    public void setUserImage3(String userImage3) {
        this.userImage3 = userImage3;
    }

    public String getUserImage4() {
        return userImage4;
    }

    public void setUserImage4(String userImage4) {
        this.userImage4 = userImage4;
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
