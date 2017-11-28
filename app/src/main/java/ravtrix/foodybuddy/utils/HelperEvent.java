package ravtrix.foodybuddy.utils;

import android.location.Location;
import android.text.format.DateFormat;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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

    public static String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time);
        return DateFormat.format("MM-dd-yyyy hh:mm a", cal).toString();
    }

    public static long getDayLeft(long eventTime) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(eventTime);

        Long currentTime = System.currentTimeMillis();
        Calendar cal2 = Calendar.getInstance(Locale.ENGLISH);
        cal2.setTimeInMillis(currentTime);

        if (cal.getTimeInMillis() - cal2.getTimeInMillis() <= 0) {
            return 0;
        }

        return  TimeUnit.MILLISECONDS.toDays(cal.getTimeInMillis() - cal2.getTimeInMillis());

    }

    public static String distanceBetweenTwoPoints(Location startPoint, Location endPoint) {
        return meterToMile(startPoint.distanceTo(endPoint));
    }

    private static String meterToMile(double meter) {
        DecimalFormat df = new DecimalFormat("#.#");
        return df.format(meter * 0.000621371192);
    }
}
