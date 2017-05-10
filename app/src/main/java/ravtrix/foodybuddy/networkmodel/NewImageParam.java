package ravtrix.foodybuddy.networkmodel;

/**
 * Created by Ravinder on 4/1/17.
 */

public class NewImageParam {

    private int user_id;
    private String url;

    public NewImageParam(int user_id, String url) {
        this.user_id = user_id;
        this.url = url;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
