package ravtrix.foodybuddy.activities.chat;

/**
 * Created by Ravinder on 5/10/17.
 */

class MessageModel {

    private int userID;
    private String text;
    private long time;
    private String imageURL;
    private String eventName;


    public MessageModel() {
    }

    public MessageModel(int userID, String text, long time, String imageURL, String eventName) {
        this.text = text;
        this.userID = userID;
        this.time = time;
        this.imageURL = imageURL;
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
