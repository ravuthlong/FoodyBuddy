package ravtrix.foodybuddy.activities.findresturant.model;

/**
 * Created by Emily on 1/31/17.
 */

public class User {
    String email, userName, password, newPassword;

    public User(String email, String userName, String password) {
        this.email = email;
        this.userName = userName;
        this.password = password;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
