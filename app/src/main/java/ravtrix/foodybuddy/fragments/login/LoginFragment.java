package ravtrix.foodybuddy.fragments.login;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.activities.mainpage.MainActivity;
import ravtrix.foodybuddy.fragments.ResetPasswordDialog;
import ravtrix.foodybuddy.fragments.register.RegisterFragment;
import ravtrix.foodybuddy.localstore.UserLocalStore;
import ravtrix.foodybuddy.model.LoggedInUser;
import ravtrix.foodybuddy.utils.LocationUtil;

import static ravtrix.foodybuddy.utils.Validation.validateEmail;
import static ravtrix.foodybuddy.utils.Validation.validateFields;

public class LoginFragment extends Fragment implements View.OnClickListener, ILoginView {

    public static final String TAG = LoginFragment.class.getSimpleName();

    @BindView(R.id.et_email) protected EditText mEtEmail;
    @BindView(R.id.et_password) protected EditText mEtPassword;
    @BindView(R.id.btn_login) protected Button mBtLogin;
    @BindView(R.id.tv_register) protected TextView mTvRegister;
    @BindView(R.id.tv_forgot_password) protected TextView mTvForgotPassword;
    @BindView(R.id.ti_email) protected TextInputLayout mTiEmail;
    @BindView(R.id.ti_password) protected TextInputLayout mTiPassword;
    @BindView(R.id.progress) protected ProgressBar mProgressBar;
    @BindView(R.id.fragment_login_bFacebook) protected LoginButton bFacebook;
    private UserLocalStore userLocalStore;
    private LoginPresenter loginPresenter;
    private CallbackManager callbackManager;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        ButterKnife.bind(this, view);
        callbackManager = CallbackManager.Factory.create();

        initListeners();
        initSharedPreference();

        loginPresenter = new LoginPresenter(this, getActivity());

        bFacebook.setFragment(this);
        bFacebook.setReadPermissions(Arrays.asList("public_profile", "email"));
        bFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                graphRequest(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
            }
        });

        LocationUtil.checkLocationPermission(getActivity());

        return view;
    }

    private void initListeners() {
        mBtLogin.setOnClickListener(this);
        mTvRegister.setOnClickListener(this);
        mTvForgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_register:
                goToRegister();
                break;
            case R.id.tv_forgot_password:
                showDialog();
                break;
            case R.id.fragment_login_bFacebook:

                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void initSharedPreference() {
        userLocalStore = new UserLocalStore(getActivity());
    }

    private void login() {

        setError();

        String email = mEtEmail.getText().toString();
        String password = mEtPassword.getText().toString();

        int err = 0;

        if (!validateEmail(email)) {
            err++;
            mTiEmail.setError("Email should be valid !");
        }

        if (!validateFields(password)) {
            err++;
            mTiPassword.setError("Password should not be empty !");
        }

        if (err == 0) {
            loginPresenter.checkUserExist(email,password);
            mProgressBar.setVisibility(View.VISIBLE);

        } else {
            showSnackbar("Enter Valid Details !");
        }
    }

    private void setError() {

        mTiEmail.setError(null);
        mTiPassword.setError(null);
    }

    private void goToRegister(){

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        RegisterFragment fragment = new RegisterFragment();
        ft.replace(R.id.fragmentFrame,fragment,RegisterFragment.TAG);
        ft.addToBackStack(null); // add fragment to back stack so user can navigate back
        ft.commit();
    }

    private void startMainActivity() {
        this.startActivity(new Intent(getActivity(), MainActivity.class));
    }

    private void showDialog(){

        ResetPasswordDialog fragment = new ResetPasswordDialog();
        fragment.show(getFragmentManager(), ResetPasswordDialog.TAG);
    }

    public void graphRequest(final AccessToken token) {

        //Make a request to fetch facebook user data based on the given token
        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {
                    final String email = object.get("email").toString();
                    final String imageURL = object.getJSONObject("picture").getJSONObject("data").get("url").toString();
                    final String firstName = object.get("last_name").toString();
                    final String lastName = object.get("first_name").toString();

                } catch (JSONException e) {e.printStackTrace();}
            }
        });
        Bundle bundle = new Bundle();
        bundle.putString("fields", "first_name,last_name, email, picture.type(large)");
        request.setParameters(bundle);
        request.executeAsync(); // which will invoke onCompleted
    }

    @Override
    public void hideProgressbar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void storeUser(LoggedInUser loggedInUser, double latitude, double longitude) {
        userLocalStore.storeUserData(loggedInUser, latitude, longitude); // token, name, email
    }

    @Override
    public void setEtEmailEmpty() {
        mEtEmail.getText().clear();
    }

    @Override
    public void setEtPasswordEmpty() {
        mEtPassword.getText().clear();
    }

    @Override
    public void startProfileActivity() {
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    @Override
    public void showSnackbar(String message) {
        if (getView() != null) {
            Snackbar.make(getView(), message, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}