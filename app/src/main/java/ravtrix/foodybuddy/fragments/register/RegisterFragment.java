package ravtrix.foodybuddy.fragments.register;

import android.app.Fragment;
import android.app.FragmentTransaction;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.fragments.login.LoginFragment;
import ravtrix.foodybuddy.model.User;

import static ravtrix.foodybuddy.utils.Validation.validateEmail;
import static ravtrix.foodybuddy.utils.Validation.validateFields;

public class RegisterFragment extends Fragment implements View.OnClickListener, IRegisterView {

    public static final String TAG = RegisterFragment.class.getSimpleName();

    @BindView(R.id.et_name) protected EditText mEtName;
    @BindView(R.id.et_email) protected EditText mEtEmail;
    @BindView(R.id.et_password) EditText mEtPassword;
    @BindView(R.id.btn_register) protected Button mBtRegister;
    @BindView(R.id.tv_login) protected TextView mTvLogin;
    @BindView(R.id.ti_name) protected TextInputLayout mTiName;
    @BindView(R.id.ti_email) protected TextInputLayout mTiEmail;
    @BindView(R.id.ti_password) protected TextInputLayout mTiPassword;
    @BindView(R.id.progress) ProgressBar mProgressbar;

    private RegisterPresenter registerPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register,container,false);
        ButterKnife.bind(this, view);
        initListeners();

        this.registerPresenter = new RegisterPresenter(this);
        return view;
    }

    private void initListeners() {
        mBtRegister.setOnClickListener(this);
        mTvLogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                register();
                break;
            case R.id.tv_login:
                goToLogin();
                break;
        }
    }

    private void register() {

        setError();

        String name = mEtName.getText().toString();
        String email = mEtEmail.getText().toString();
        String password = mEtPassword.getText().toString();

        int err = 0;

        if (!validateFields(name)) {

            err++;
            mTiName.setError("Name should not be empty !");
        }

        if (!validateEmail(email)) {

            err++;
            mTiEmail.setError("Email should be valid !");
        }

        if (!validateFields(password)) {

            err++;
            mTiPassword.setError("Password should not be empty !");
        }

        if (err == 0) {

            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);

            mProgressbar.setVisibility(View.VISIBLE);
            this.registerPresenter.register(user);

        } else {
            showSnackbar("Enter Valid Details !");
        }
    }

    private void setError() {

        mTiName.setError(null);
        mTiEmail.setError(null);
        mTiPassword.setError(null);
    }

    private void goToLogin(){

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        LoginFragment fragment = new LoginFragment();
        ft.replace(R.id.fragmentFrame, fragment, LoginFragment.TAG);
        ft.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        registerPresenter.unsubscribe();
    }

    @Override
    public void hideProgressbar() {
        mProgressbar.setVisibility(View.GONE);
    }

    @Override
    public void showSnackbar(String message) {
        if (getView() != null) {
            Snackbar.make(getView(),message,Snackbar.LENGTH_SHORT).show();
        }
    }
}