package ravtrix.foodybuddy.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.activities.mainpage.MainActivity;
import ravtrix.foodybuddy.fragments.ResetPasswordDialog;
import ravtrix.foodybuddy.fragments.login.LoginFragment;
import ravtrix.foodybuddy.localstore.UserLocalStore;
import ravtrix.foodybuddy.utils.Helpers;

public class LoginActivity extends AppCompatActivity implements ResetPasswordDialog.Listener {

    public static final String TAG = LoginActivity.class.getSimpleName();

    private LoginFragment mLoginFragment;
    private ResetPasswordDialog mResetPasswordDialog;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppEventsLogger.activateApp(getApplication());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_login);

        userLocalStore = new UserLocalStore(this);

        // Skip log in page if user logged in already
        if (userLocalStore.isUserLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            if (savedInstanceState == null) {
                loadLoginFragment();
            }
        }
    }

    private void loadLoginFragment(){

        if (mLoginFragment == null) {
            mLoginFragment = new LoginFragment();
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.fragmentFrame, mLoginFragment, LoginFragment.TAG)
                .commit();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        String data = intent.getData().getLastPathSegment();
        Log.d(TAG, "onNewIntent: "+data);

        mResetPasswordDialog = (ResetPasswordDialog) getFragmentManager().findFragmentByTag(ResetPasswordDialog.TAG);

        if (mResetPasswordDialog != null)
            mResetPasswordDialog.setToken(data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Helpers.displayToast(this, "PARENT");

    }

    @Override
    public void onPasswordReset(String message) {

        showSnackBarMessage(message);
    }

    private void showSnackBarMessage(String message) {

        Snackbar.make(findViewById(R.id.activity_login),message,Snackbar.LENGTH_SHORT).show();
    }
}