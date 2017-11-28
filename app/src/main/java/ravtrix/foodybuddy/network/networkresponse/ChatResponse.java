package ravtrix.foodybuddy.network.networkresponse;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by Ravinder on 5/11/17.
 */

public class ChatResponse implements Comparable<ChatResponse> {

    private int chat_group_id;
    private int event_id;
    private int user_id;
    private int creator_id;
    private String creator_pic;
    private String rest_image;
    private String latest_message;
    private String latestDate;
    private String eventName;
    private transient DataSnapshot snapshot;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getLatest_message() {
        return latest_message;
    }

    public void setLatest_message(String latest_message) {
        this.latest_message = latest_message;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public String getCreator_pic() {
        return creator_pic;
    }

    public void setCreator_pic(String creator_pic) {
        this.creator_pic = creator_pic;
    }

    public String getRest_image() {
        return rest_image;
    }

    public void setRest_image(String rest_image) {
        this.rest_image = rest_image;
    }

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

    public String getLatestDate() {
        return latestDate;
    }

    public void setLatestDate(String latestDate) {
        this.latestDate = latestDate;
    }

    public DataSnapshot getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(DataSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    @Override
    public int compareTo(@NonNull ChatResponse chatResponse) {
        long anotherTime = Long.parseLong(chatResponse.latestDate);
        //descending order
        return anotherTime < Long.parseLong(this.latestDate) ? -1 : anotherTime == Long.parseLong(this.latestDate) ? 0 : 1;
    }
}
