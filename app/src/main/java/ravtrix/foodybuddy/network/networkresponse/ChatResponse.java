package ravtrix.foodybuddy.network.networkresponse;

/**
 * Created by Ravinder on 5/11/17.
 */

public class ChatResponse {

    private int chat_group_id;
    private int event_id;
    private int user_id;

    public int getChat_group_id() {
        return chat_group_id;
    }

    public void setChat_group_id(int chat_group_id) {
        this.chat_group_id = chat_group_id;
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
