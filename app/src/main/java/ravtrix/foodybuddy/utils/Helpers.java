package ravtrix.foodybuddy.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

/**
 * Created by Ravinder on 1/28/17.
 */

public class Helpers {

    private Helpers() {}

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
     * Create an alert dialog with positive buttons
     * @param activity      the activity for the dialog
     * @param message       the message to be displayed
     * @param negative      the text for negative button
     */
    public static AlertDialog.Builder showAlertDialogWithTwoOptions(final android.app.Activity activity, String title, String message,
                                                                    String negative) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);
        dialogBuilder.setNegativeButton(negative, null);
        return dialogBuilder;
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
    public static void displayErrorSnackbar(Context context, View parentView) {
        if (!isConnectedToInternet(context)) {
            makeSnackbar(parentView, "Not Connected to the Internet");
        } else {
            makeSnackbar(parentView, "Problem Loading");
        }
    }

    private static void makeSnackbar(View parentView, String message) {
        Snackbar snackbar = Snackbar
                .make(parentView, message, Snackbar.LENGTH_SHORT);
        View mView = snackbar.getView();
        TextView mTextView = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        snackbar.show();
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


    public static void hideKeyboard(Activity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Scale image
     * @param selectedImage     the image selected by user
     * @return                  scaled version of bitmap
     * @throws FileNotFoundException
     */
    public static Bitmap decodeBitmap(Activity activity, Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 300;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(selectedImage), null, o2);
    }

    public static String getBase64ProfileImage(ImageView imageView) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ((BitmapDrawable) imageView
                .getDrawable())
                .getBitmap()
                .compress(Bitmap
                        .CompressFormat
                        .JPEG, 60, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

}
