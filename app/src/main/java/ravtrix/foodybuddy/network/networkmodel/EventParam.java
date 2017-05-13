package ravtrix.foodybuddy.network.networkmodel;

/**
 * Created by Ravinder on 3/10/17.
 */

public class EventParam {

    private int user_id;
    private int event_id;
    private String rest_id;

    public EventParam(int userID, int eventID, String restID) {
        this.user_id = userID;
        this.event_id = eventID;
        this.rest_id = restID;
    }

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

    public String getRest_id() {
        return rest_id;
    }

    public void setRest_id(String rest_id) {
        this.rest_id = rest_id;
    }
}
