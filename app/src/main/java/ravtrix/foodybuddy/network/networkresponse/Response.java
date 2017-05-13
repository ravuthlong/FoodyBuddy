package ravtrix.foodybuddy.network.networkresponse;

/**
 * Created by Ravinder on 3/4/17.
 * Used by retrofit for response callback
 */

public class Response {
    private String message;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
