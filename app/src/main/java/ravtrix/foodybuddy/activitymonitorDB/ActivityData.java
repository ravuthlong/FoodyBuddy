package ravtrix.foodybuddy.activitymonitorDB;

import android.provider.BaseColumns;

/**
 * Created by rlong on 11/8/17.
 */

public class ActivityData {

    public ActivityData() {}

    public static abstract class TableInfo implements BaseColumns {

        public static final String ACTIVITY_TIME = "activity_time";
        public static final String ACTIVITY_DETAIL = "activity_detail";
        public static final String DB_NAME = "activity_db";
        public static final String DB_TABLE = "activity_table";

    }
}
