package ravtrix.foodybuddy.fragments.register;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.fragments.login.LoginFragment;
import ravtrix.foodybuddy.model.User;
import ravtrix.foodybuddy.utils.Helpers;

import static android.app.Activity.RESULT_OK;
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
    @BindView(R.id.progress) protected ProgressBar mProgressbar;
    @BindView(R.id.frag_register_imgProfileImage) protected ImageView profilePic;

    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    private static final int RESULT_LOAD_IMAGE = 1;
    private RegisterPresenter registerPresenter;
    private Context context;
    private boolean isPhotoUploaed = false;
    private String bitmapImageBase64;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register,container,false);
        context = getActivity();
        ButterKnife.bind(this, view);
        initListeners();

        this.registerPresenter = new RegisterPresenter(this);
        return view;
    }

    private void initListeners() {
        mBtRegister.setOnClickListener(this);
        mTvLogin.setOnClickListener(this);
        profilePic.setOnClickListener(this);
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
            case R.id.frag_register_imgProfileImage:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                        && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                            getString(R.string.permission_title_rationale),
                            REQUEST_STORAGE_READ_ACCESS_PERMISSION);
                } else {
                    // Get the image from gallery
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                }
                break;
        }
    }

    private void register() {

        //registerPresenter.uploadImage(getBase64());


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

        if (!isPhotoUploaed) {

            err++;
            Helpers.displayToast(context, "Choose a profile picture");
        }

        if (err == 0) {

            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setBase64Image(Helpers.getBase64ProfileImage(profilePic));

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
        System.out.println(message);
        if (getView() != null) {
            Snackbar.make(getView(), message,Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * Requests given permission.
     * If the permission has been denied previously, a Dialog will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
            showAlertDialog(getString(R.string.permission_title_rationale), rationale,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context,
                                    new String[]{permission}, requestCode);
                        }
                    }, getString(R.string.label_ok), null, getString(R.string.label_cancel));
        } else {
            // Request permission, here a dialog should pop up asking for permission
            if (android.os.Build.VERSION.SDK_INT >= 23) {
                this.requestPermissions(new String[]{permission}, requestCode);
            }
        }
    }

    // Case for users with grant access needed. Location access granted.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case RegisterFragment.REQUEST_STORAGE_READ_ACCESS_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Start intent to pick image once again
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            this.isPhotoUploaed = true;
            Uri selectedImage = data.getData();
            Bitmap bitmapImage = null;

            try {
                bitmapImage = Helpers.decodeBitmap((Activity) context, selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            profilePic.setImageBitmap(bitmapImage);
            //imgRotate.setVisibility(View.VISIBLE);
        }
    }

    protected void showAlertDialog(String title, String message,
                                   DialogInterface.OnClickListener onPositiveButtonClickListener,
                                   String positiveText,
                                   DialogInterface.OnClickListener onNegativeButtonClickListener,
                                   String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        builder.show();
    }
}