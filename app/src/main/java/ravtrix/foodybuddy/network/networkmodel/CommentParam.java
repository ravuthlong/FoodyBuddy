package ravtrix.foodybuddy.network.networkmodel;

/**
 * Created by Ravinder on 3/27/17.
 */

public class CommentParam {

    private int user_id;
    private int event_id;
    private String comment;
    private long create_time;

    public CommentParam() {}

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }
}
