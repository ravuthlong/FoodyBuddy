package ravtrix.foodybuddy.fragments.eventprofilefrag.recycylerviewtimeline;

/**
 * Created by rlong on 11/8/17.
 */

public class ActivityModel {

    private String activity_time;
    private String activity_detail;

    public ActivityModel(String activity_time, String activity_detail) {
        this.activity_detail = activity_detail;
        this.activity_time = activity_time;
    }

    public String getActivity_time() {
        return activity_time;
    }

    public void setActivity_time(String activity_time) {
        this.activity_time = activity_time;
    }

    public String getActivity_detail() {
        return activity_detail;
    }

    public void setActivity_detail(String activity_detail) {
        this.activity_detail = activity_detail;
    }
}
