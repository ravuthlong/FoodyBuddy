package ravtrix.foodybuddy.utils;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import ravtrix.foodybuddy.R;

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

    /**
     * Requests given permission.
     * If the permission has been denied previously, a Dialog will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    public static void requestPhotoAccessPermission(final Activity activity, final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            showAlertDialog(activity, activity.getString(R.string.permission_title_rationale), rationale,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{permission}, requestCode);
                        }
                    }, activity.getString(R.string.label_ok), null, activity.getString(R.string.label_cancel));
        } else {
            // Request permission, here a dialog should pop up asking for permission
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                activity.requestPermissions(new String[]{permission}, requestCode);
            }
        }
    }

    private static void showAlertDialog(Activity activity, String title, String message,
                                   DialogInterface.OnClickListener onPositiveButtonClickListener,
                                   String positiveText,
                                   DialogInterface.OnClickListener onNegativeButtonClickListener,
                                   String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        builder.show();
    }
}
