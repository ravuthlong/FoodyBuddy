package ravtrix.foodybuddy.activities.mainpage.model;

/**
 * Created by Ravinder on 3/10/17.
 */

public class EventJoined {

    private int joined_event_id;
    private int user_id;
    private int event_id;
    private String rest_id;
    private String rest_name;
    private String event_des;
    private String address;
    private String lat;
    private String lng;
    private long event_time;
    private long create_time;

    public String getRest_name() {
        return rest_name;
    }

    public void setRest_name(String rest_name) {
        this.rest_name = rest_name;
    }

    public int getJoined_event_id() {
        return joined_event_id;
    }

    public void setJoined_event_id(int joined_event_id) {
        this.joined_event_id = joined_event_id;
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

    public String getEvent_des() {
        return event_des;
    }

    public void setEvent_des(String event_des) {
        this.event_des = event_des;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public long getEvent_time() {
        return event_time;
    }

    public void setEvent_time(long event_time) {
        this.event_time = event_time;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }
}
