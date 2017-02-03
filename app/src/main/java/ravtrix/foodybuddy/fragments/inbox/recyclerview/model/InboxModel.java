package ravtrix.foodybuddy.fragments.inbox.recyclerview.model;

/**
 * Created by Ravinder on 1/28/17.
 */

public class InboxModel {

    private String username, imageURL, time, message;

    public InboxModel(String username, String imageURL, String time, String message) {
        this.username = username;
        this.imageURL = imageURL;
        this.time = time;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
