package ravtrix.foodybuddy.fragments;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.foodybuddy.activities.login.LoginActivity;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.network.networkresponse.LogInResponse;
import ravtrix.foodybuddy.model.User;
import ravtrix.foodybuddy.utils.NetworkUtil;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static ravtrix.foodybuddy.utils.Validation.validateEmail;
import static ravtrix.foodybuddy.utils.Validation.validateFields;

public class ResetPasswordDialog extends DialogFragment implements View.OnClickListener {

    public interface Listener {

        void onPasswordReset(String message);
    }

    public static final String TAG = ResetPasswordDialog.class.getSimpleName();

    @BindView(R.id.et_email) protected EditText mEtEmail;
    @BindView(R.id.et_token) protected EditText mEtToken;
    @BindView(R.id.et_password) protected EditText mEtPassword;
    @BindView(R.id.btn_reset_password) protected Button mBtResetPassword;
    @BindView(R.id.tv_message) protected TextView mTvMessage;
    @BindView(R.id.ti_email) protected TextInputLayout mTiEmail;
    @BindView(R.id.ti_token) protected TextInputLayout mTiToken;
    @BindView(R.id.ti_password) protected TextInputLayout mTiPassword;
    @BindView(R.id.progress) protected ProgressBar mProgressBar;

    private CompositeSubscription mSubscriptions;
    private String mEmail;
    private boolean isInit = true;
    private Listener mListner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_reset_password,container,false);
        ButterKnife.bind(this, view);
        mSubscriptions = new CompositeSubscription();
        initListeners();
        return view;
    }

    private void initListeners() {
        mBtResetPassword.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListner = (LoginActivity)context;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reset_password:
                if (isInit) resetPasswordInit();
                else resetPasswordFinish();
                break;
        }
    }

    private void setEmptyFields() {

        mTiEmail.setError(null);
        mTiToken.setError(null);
        mTiPassword.setError(null);
        mTvMessage.setText(null);
    }

    public void setToken(String token) {
        mEtToken.setText(token);
    }

    private void resetPasswordInit() {

        setEmptyFields();

        mEmail = mEtEmail.getText().toString();

        int err = 0;

        if (!validateEmail(mEmail)) {

            err++;
            mTiEmail.setError("Email Should be Valid !");
        }

        if (err == 0) {

            mProgressBar.setVisibility(View.VISIBLE);
            resetPasswordInitProgress(mEmail);
        }
    }

    private void resetPasswordFinish() {

        setEmptyFields();

        String token = mEtToken.getText().toString();
        String password = mEtPassword.getText().toString();

        int err = 0;

        if (!validateFields(token)) {

            err++;
            mTiToken.setError("Token Should not be empty !");
        }

        if (!validateFields(password)) {

            err++;
            mTiEmail.setError("Password Should not be empty !");
        }

        if (err == 0) {

            mProgressBar.setVisibility(View.VISIBLE);

            User user = new User();
            user.setPassword(password);
            user.setToken(token);
            resetPasswordFinishProgress(user);
        }
    }

    private void resetPasswordInitProgress(String email) {

        mSubscriptions.add(NetworkUtil.getRetrofit().resetPasswordInit(email)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<LogInResponse>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        handleError(e);
                    }

                    @Override
                    public void onNext(LogInResponse logInResponse) {
                        handleResponse(logInResponse);
                    }}));
    }

    private void resetPasswordFinishProgress(User user) {

        mSubscriptions.add(NetworkUtil.getRetrofit().resetPasswordFinish(mEmail,user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<LogInResponse>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        handleError(e);
                    }

                    @Override
                    public void onNext(LogInResponse logInResponse) {
                        handleResponse(logInResponse);
                    }
                }));
    }

    private void handleResponse(LogInResponse logInResponse) {

        mProgressBar.setVisibility(View.GONE);

        if (isInit) {

            isInit = false;
            showMessage(logInResponse.getMessage());
            mTiEmail.setVisibility(View.GONE);
            mTiToken.setVisibility(View.VISIBLE);
            mTiPassword.setVisibility(View.VISIBLE);

        } else {

            mListner.onPasswordReset(logInResponse.getMessage());
            dismiss();
        }
    }

    private void handleError(Throwable error) {

        mProgressBar.setVisibility(View.GONE);

        if (error instanceof HttpException) {

            Gson gson = new GsonBuilder().create();

            try {

                String errorBody = ((HttpException) error).response().errorBody().string();
                LogInResponse logInResponse = gson.fromJson(errorBody,LogInResponse.class);
                showMessage(logInResponse.getMessage());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            showMessage("Network Error !");
        }
    }

    private void showMessage(String message) {

        mTvMessage.setVisibility(View.VISIBLE);
        mTvMessage.setText(message);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }
}
