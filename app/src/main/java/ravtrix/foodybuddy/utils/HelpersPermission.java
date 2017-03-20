package ravtrix.foodybuddy.utils;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Ravinder on 3/19/17.
 */

public class HelpersPermission {
    private static final int LOCATION_REQUEST_CODE = 10;

    private HelpersPermission() {}

    public static void showLocationRequest(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION }, LOCATION_REQUEST_CODE);
    }
}
