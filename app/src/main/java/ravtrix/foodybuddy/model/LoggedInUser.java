package ravtrix.foodybuddy.model;

/**
 * Created by Ravinder on 3/3/17.
 */

public class LoggedInUser {

    private String token;
    private int user_id;
    private String email;
    private String imageURL;

    public LoggedInUser(String token, int userID, String email, String imageURL) {
        this.token = token;
        this.user_id = userID;
        this.email = email;
        this.imageURL = imageURL;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserID() {
        return user_id;
    }

    public void setUserID(int userID) {
        this.user_id = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
