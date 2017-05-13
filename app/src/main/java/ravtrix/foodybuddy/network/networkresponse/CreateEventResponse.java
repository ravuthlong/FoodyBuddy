package ravtrix.foodybuddy.network.networkresponse;

/**
 * Created by Ravinder on 5/11/17.
 */

public class CreateEventResponse {

    private int status;
    private int message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }
}
