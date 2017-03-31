package ravtrix.foodybuddy.activities.editprofileimage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.activities.editprofileimage.model.ProfileImageModel;
import ravtrix.foodybuddy.localstore.UserLocalStore;
import ravtrix.foodybuddy.model.ImageResponse;
import ravtrix.foodybuddy.network.retrofitrequests.RetrofitPhoto;
import ravtrix.foodybuddy.utils.Helpers;
import ravtrix.foodybuddy.utils.HelpersPermission;
import ravtrix.foodybuddy.utils.RetrofitUserInfoSingleton;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class EditProfileImageActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.activity_edit_profile_image_profileImage) protected ImageView profileImage;
    @BindView(R.id.activity_edit_profile_image_tvEdit) protected TextView tvEdit;
    private CompositeSubscription mSubscriptions;
    private UserLocalStore userLocalStore;
    protected static final int REQUEST_STORAGE_READ_ACCESS_PERMISSION = 101;
    private static final int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_image);
        ButterKnife.bind(this);

        Helpers.overrideFonts(this, tvEdit);
        setListeners();

        mSubscriptions = new CompositeSubscription();
        userLocalStore = new UserLocalStore(this);
        fetchProfileImage();
    }

    private void setListeners() {
        tvEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_edit_profile_image_tvEdit:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    HelpersPermission.requestPhotoAccessPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE,
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

    private void fetchProfileImage() {

        mSubscriptions.add(RetrofitUserInfoSingleton.getRetrofitUserInfo()
                    .getAUserPhoto()
                    .getAUserPhoto(userLocalStore.getLoggedInUser().getUserID())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<ProfileImageModel>() {
                        @Override
                        public void onCompleted() {}

                        @Override
                        public void onError(Throwable e) {
                            Log.d(EditProfileImageActivity.class.getSimpleName(), "Error fetching photo");
                        }

                        @Override
                        public void onNext(ProfileImageModel profileImageModel) {

                            String userImage;

                            if (!profileImageModel.getUrl().isEmpty()) {
                                userImage = profileImageModel.getUrl();
                            } else {
                                userImage = "http://s3.amazonaws.com/37assets/svn/765-default-avatar.png";
                            }

                            Picasso.with(EditProfileImageActivity.this)
                                    .load(userImage)
                                    .fit()
                                    .centerCrop()
                                    .into(profileImage);
                        }
                    }));

    }

    // Case for users with grant access needed. Location access granted.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case EditProfileImageActivity.REQUEST_STORAGE_READ_ACCESS_PERMISSION:
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
            Uri selectedImage = data.getData();
            Bitmap bitmapImage = null;

            try {
                bitmapImage = Helpers.decodeBitmap(this, selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            profileImage.setImageBitmap(bitmapImage);
            uploadImage(Helpers.getBase64ProfileImage(profileImage));
            //imgRotate.setVisibility(View.VISIBLE);
        }
    }

    public void uploadImage(final String image) {

        mSubscriptions.add(new RetrofitPhoto().uploadImage().uploadImage(image)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ImageResponse>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                       Log.e(EditProfileImageActivity.class.getSimpleName(), "Error uploading image");
                    }

                    @Override
                    public void onNext(ImageResponse imageResponse) {
                        Log.d(EditProfileImageActivity.class.getSimpleName(), imageResponse.getURL());
                    }
                }));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }
}
