package ravtrix.foodybuddy.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Ravinder on 1/28/17.
 */

public class Helpers {

    public static final String YELP_TOKEN = "zeAZ8CvH8Jh5BXgKlgomA80NvCM_dK_rZahtOnEivGo4GSypKzbRQHA5GlbKppo9NHqmYOq0AFZcKh0gKaYifBQQ0EeX7ids2FTfE0GK5PWunl1BHbmUfyZLhieQWHYx";
    private Helpers() {}

    public static final class ServerURL {
        public static final String SERVER_URL = "URL TO BE";
        public static final String YELP_API_URL = "https://api.yelp.com";
    }

    private static OkHttpClient okClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        // Customizing a default header for Yelp API because Yelp GET request requires an access token as header
                        // before it can validate and fetch from the Yelp database
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("Authorization", "Bearer " + YELP_TOKEN)
                                .method(original.method(), original.body())
                                .build();

                        return chain.proceed(request);
                    }
                });
        return httpClient.build();
    }

    /**
     * Create a retrofit object
     * @param serverURL       the url to the server
     */
    public static Retrofit retrofitBuilder(String serverURL)  {

        return new Retrofit.Builder()
                .baseUrl(serverURL)
                .client(okClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
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


    public static void hideKeyboard(Activity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
