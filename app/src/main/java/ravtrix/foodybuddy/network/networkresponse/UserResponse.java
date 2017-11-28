package ravtrix.foodybuddy.network.networkresponse;

/**
 * Created by rlong on 11/1/17.
 */

public class UserResponse {

    private String username;
    private String email;
    private String profile_pic_url;
    private String event_created;
    private String event_joined;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_pic_url() {
        return profile_pic_url;
    }

    public void setProfile_pic_url(String profile_pic_url) {
        this.profile_pic_url = profile_pic_url;
    }

    public String getEvent_created() {
        return event_created;
    }

    public void setEvent_created(String event_created) {
        this.event_created = event_created;
    }

    public String getEvent_joined() {
        return event_joined;
    }

    public void setEvent_joined(String event_joined) {
        this.event_joined = event_joined;
    }
}
