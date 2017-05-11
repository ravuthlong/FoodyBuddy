package ravtrix.foodybuddy.activities.chat;

/**
 * Created by Ravinder on 5/10/17.
 */

class MessageModel {

    private int userID;
    private String text;
    private long time;

    public MessageModel() {
    }

    public MessageModel(int userID, String text, long time) {
        this.text = text;
        this.userID = userID;
        this.time = time;
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
