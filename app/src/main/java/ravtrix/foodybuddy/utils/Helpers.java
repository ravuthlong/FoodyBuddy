package ravtrix.foodybuddy.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Ravinder on 1/28/17.
 */

public class Helpers {

    private Helpers() {}

    public static final class ServerURL {
        public static final String SERVER_URL = "URL TO BE";
    }

    /**
     * Set the toolbar to an activity ot fragment
     * @param appCompatActivity         - the activity
     * @param toolbar                   - the toolbar for activity/fragment
     */
    public static void setToolbar(final AppCompatActivity appCompatActivity, Toolbar toolbar) {

        appCompatActivity.setSupportActionBar(toolbar);

        if (appCompatActivity.getSupportActionBar() != null) {
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appCompatActivity.onBackPressed();
                }
            });
        }
    }

    /**
     * Used to override all sub views to Text.ttf
     * @param context           - the context of view
     * @param v                 - the parent view
     */
    public static void overrideFonts(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "Text.ttf"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a progress dialog object
     * @param context           - the context to display the dialog
     * @param message           - the message to be displayed
     * @return                  - the progress dialog object
     */
    public static ProgressDialog showProgressDialog(Context context, String message) {
        ProgressDialog pDialog = new ProgressDialog(context);
        pDialog.setMessage(message);
        pDialog.setCancelable(false);
        pDialog.show();
        return pDialog;
    }

    /**
     * Hide progress dialog
     * @param pDialog           - the progress dialog to be hidden
     */
    public static void hideProgressDialog(ProgressDialog pDialog) {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    /**
     * Display a toast on the screen
     * @param context           - the context to display the toast
     * @param string            - the message to be diaplyed
     */
    public static void displayToast(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }

    /**
     * Display error toast based on error
     * @param context           - the context to display
     */
    public static void displayErrorToast(Context context) {
        if (!isConnectedToInternet(context)) {
            Helpers.displayToast(context, "Not connected to the internet");
        } else {
            Helpers.displayToast(context, "Problem loading...");
        }
    }

    /**
     * Check network connection
     * @param context       the context of network check
     * @return              true is the user is connected to the internet, false otherwise
     */
    private static boolean isConnectedToInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null;
    }
}
