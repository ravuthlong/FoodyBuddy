package ravtrix.foodybuddy.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ravtrix.foodybuddy.activities.mainpage.model.EventJoined;

/**
 * Created by rlong on 9/3/17.
 */

public class HelperEvent {

    public static List<EventJoined> sortJoinedEvents(List<EventJoined> events) {
        Collections.sort(events, new Comparator<EventJoined>() {
            @Override
            public int compare(EventJoined o1, EventJoined o2) {
                return Long.compare(o1.getEvent_time(), o2.getEvent_time());
            }
        });

        return events;
    }
}
