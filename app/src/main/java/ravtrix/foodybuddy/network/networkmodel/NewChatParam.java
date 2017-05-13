package ravtrix.foodybuddy.network.networkmodel;

/**
 * Created by Ravinder on 5/11/17.
 */

public class NewChatParam {

    private int event_id;
    private int user_id;

    public NewChatParam(int user_id, int event_id) {
        this.user_id = user_id;
        this.event_id = event_id;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
