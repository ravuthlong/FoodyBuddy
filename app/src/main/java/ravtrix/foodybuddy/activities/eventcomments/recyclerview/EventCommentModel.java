package ravtrix.foodybuddy.activities.eventcomments.recyclerview;

/**
 * Created by Ravinder on 2/9/17.
 */

public class EventCommentModel {

    private String username, comment, time, userImage;

    public EventCommentModel(String username, String comment, String time, String userImage) {
        this.username = username;
        this.comment = comment;
        this.time = time;
        this.userImage = userImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
