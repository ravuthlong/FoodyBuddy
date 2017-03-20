package ravtrix.foodybuddy.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.activities.login.LoginActivity;
import ravtrix.foodybuddy.fragments.ChangePasswordDialog;
import ravtrix.foodybuddy.localstore.UserLocalStore;
import ravtrix.foodybuddy.model.LogInResponse;
import ravtrix.foodybuddy.model.User;
import ravtrix.foodybuddy.utils.NetworkUtil;
import ravtrix.foodybuddy.utils.Constants;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class ProfileActivity extends AppCompatActivity implements ChangePasswordDialog.Listener, View.OnClickListener {

    public static final String TAG = ProfileActivity.class.getSimpleName();

    @BindView(R.id.tv_name) protected TextView mTvName;
    @BindView(R.id.tv_email) protected TextView mTvEmail;
    @BindView(R.id.tv_date) protected TextView mTvDate;
    @BindView(R.id.btn_change_password) protected Button mBtChangePassword;
    @BindView(R.id.btn_logout) protected Button mBtLogout;
    @BindView(R.id.progress) protected ProgressBar mProgressbar;

    private UserLocalStore userLocalStore;
    private String mToken;
    private String mEmail;

    private CompositeSubscription mSubscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        mSubscriptions = new CompositeSubscription();
        initViews();
        initSharedPreference();
        loadProfile();
    }

    private void initViews() {
        mBtChangePassword.setOnClickListener(this);
        mBtLogout.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change_password:
                showDialog();
                break;
            case R.id.btn_logout:
                logout();
                break;
        }
    }

    private void initSharedPreference() {
        userLocalStore = new UserLocalStore(this);
        //int token = userLocalStore.getLoggedInUser().getUserID();
        mEmail = userLocalStore.getLoggedInUser().getEmail();
    }

    private void logout() {
        userLocalStore.clearUserData();
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void showDialog(){

        ChangePasswordDialog fragment = new ChangePasswordDialog();

        Bundle bundle = new Bundle();
        bundle.putString(Constants.EMAIL, mEmail);
        bundle.putString(Constants.TOKEN,mToken);
        fragment.setArguments(bundle);

        fragment.show(getFragmentManager(), ChangePasswordDialog.TAG);
    }

    private void loadProfile() {

        mSubscriptions.add(NetworkUtil.getRetrofit(mToken).getProfile(mEmail)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        handleError(e);
                    }

                    @Override
                    public void onNext(User user) {
                        handleResponse(user);
                    }
                }));
    }

    private void handleResponse(User user) {

        mProgressbar.setVisibility(View.GONE);
        mTvName.setText(user.getName());
        mTvEmail.setText(user.getEmail());
        mTvDate.setText(user.getCreated_at());
    }

    private void handleError(Throwable error) {

        mProgressbar.setVisibility(View.GONE);

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                LogInResponse logInResponse = gson.fromJson(errorBody,LogInResponse.class);
                showSnackBarMessage(logInResponse.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            showSnackBarMessage("Network Error !");
        }
    }

    private void showSnackBarMessage(String message) {

        Snackbar.make(findViewById(R.id.activity_profile),message,Snackbar.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

    @Override
    public void onPasswordChanged() {

        showSnackBarMessage("Password Changed Successfully !");
    }
}
