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
import ravtrix.foodybuddy.activities.ProfileActivity;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.model.LogInResponse;
import ravtrix.foodybuddy.model.User;
import ravtrix.foodybuddy.network.NetworkUtil;
import ravtrix.foodybuddy.utils.Constants;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static ravtrix.foodybuddy.utils.Validation.validateFields;

public class ChangePasswordDialog extends DialogFragment implements View.OnClickListener {

    public interface Listener {
        void onPasswordChanged();
    }

    public static final String TAG = ChangePasswordDialog.class.getSimpleName();

    @BindView(R.id.et_old_password) protected EditText mEtOldPassword;
    @BindView(R.id.et_new_password) protected EditText mEtNewPassword;
    @BindView(R.id.btn_change_password) protected Button mBtChangePassword;
    @BindView(R.id.btn_cancel) protected Button mBtCancel;
    @BindView(R.id.tv_message) protected TextView mTvMessage;
    @BindView(R.id.ti_old_password) protected TextInputLayout mTiOldPassword;
    @BindView(R.id.ti_new_password) TextInputLayout mTiNewPassword;
    @BindView(R.id.progress) protected ProgressBar mProgressBar;

    private CompositeSubscription mSubscriptions;

    private String mToken;
    private String mEmail;
    private Listener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_change_password,container,false);
        ButterKnife.bind(this, view);

        mSubscriptions = new CompositeSubscription();
        getData();
        initListeners();

        return view;
    }

    private void getData() {

        Bundle bundle = getArguments();

        mToken = bundle.getString(Constants.TOKEN);
        mEmail = bundle.getString(Constants.EMAIL);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (ProfileActivity)context;
    }

    private void initListeners() {

        mBtChangePassword.setOnClickListener(this);
        mBtCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change_password:
                changePassword();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    private void changePassword() {

        setError();

        String oldPassword = mEtOldPassword.getText().toString();
        String newPassword = mEtNewPassword.getText().toString();

        int err = 0;

        if (!validateFields(oldPassword)) {

            err++;
            mTiOldPassword.setError("Password should not be empty !");
        }

        if (!validateFields(newPassword)) {

            err++;
            mTiNewPassword.setError("Password should not be empty !");
        }

        if (err == 0) {

            User user = new User();
            user.setPassword(oldPassword);
            user.setNewPassword(newPassword);
            changePasswordProgress(user);
            mProgressBar.setVisibility(View.VISIBLE);

        }
    }

    private void setError() {

        mTiOldPassword.setError(null);
        mTiNewPassword.setError(null);
    }

    private void changePasswordProgress(User user) {

        mSubscriptions.add(NetworkUtil.getRetrofit(mToken).changePassword(mEmail,user)
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

    private void handleResponse(LogInResponse logInResponse) {

        mProgressBar.setVisibility(View.GONE);
        mListener.onPasswordChanged();
        dismiss();
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