package ravtrix.foodybuddy.model;

/**
 * Created by Emily on 2/6/17.
 */

public class User {

    private String name;
    private String email;
    private String password;
    private String created_at;
    private String newPassword;
    private String token;

    public User() {}

    public User(String token, String name, String email) {
        this.token = token;
        this.name = name;
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getToken() {
        return token;
    }
}