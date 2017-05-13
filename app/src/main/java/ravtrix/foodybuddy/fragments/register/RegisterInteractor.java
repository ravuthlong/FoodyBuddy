package ravtrix.foodybuddy.fragments.register;

import android.util.Log;

import ravtrix.foodybuddy.activities.editprofileimage.EditProfileImageActivity;
import ravtrix.foodybuddy.network.networkresponse.ImageResponse;
import ravtrix.foodybuddy.network.networkresponse.RegisterResponse;
import ravtrix.foodybuddy.model.User;
import ravtrix.foodybuddy.network.retrofitrequests.RetrofitPhoto;
import ravtrix.foodybuddy.network.networkmodel.NewImageParam;
import ravtrix.foodybuddy.utils.NetworkUtil;
import ravtrix.foodybuddy.utils.RetrofitUserInfoSingleton;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Ravinder on 2/19/17.
 */

class RegisterInteractor implements IRegisterInteractor {

    private CompositeSubscription mSubscriptions;
    private static final String CLASS_NAME = RegisterInteractor.class.getSimpleName();
    private RegisterResponse registerResponseObj;

    RegisterInteractor() {
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void registerProcess(User user, final String bitmap, final OnRetrofitImageFinished onRetrofitFinished) {
        mSubscriptions.add(NetworkUtil.getRetrofit().register(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<RegisterResponse>() {
                    @Override
                    public void onCompleted() {
                        onRetrofitFinished.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        onRetrofitFinished.onError(e);
                    }

                    @Override
                    public void onNext(RegisterResponse registerResponse) {
                        //onRetrofitFinished.onNext(registerResponse);
                        registerResponseObj = registerResponse;
                        uploadImageIMGUR(registerResponse.getMessage(), bitmap, onRetrofitFinished);
                    }
                }));
    }

    @Override
    public void uploadImageIMGUR(final int userID, final String imageBitmap, final OnRetrofitImageFinished onRetrofitImageFinished) {

        mSubscriptions.add(new RetrofitPhoto().uploadImage().uploadImage(imageBitmap)
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
                        // After image has been uploaded to imgur, insert it into the database table for user image
                        insertImage(userID, imageResponse.getURL(), onRetrofitImageFinished);
                        Log.d(EditProfileImageActivity.class.getSimpleName(), imageResponse.getURL());
                    }
                }));

    }

    private void insertImage(int userID, String url, final OnRetrofitImageFinished onRetrofitImageFinished) {

        mSubscriptions.add(RetrofitUserInfoSingleton.getInstance()
                .insertUserImage()
                .insertAUserImage(new NewImageParam(userID, url))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Void>() {
                    @Override
                    public void onCompleted() {
                        Log.d(CLASS_NAME, "On complete called for inserting image");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(CLASS_NAME, "Error inserting image");
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        Log.d(CLASS_NAME, "Successfully inserted image");
                        onRetrofitImageFinished.onNext(registerResponseObj);
                    }
                }));

    }

    @Override
    public void unsubscribe() {
        mSubscriptions.unsubscribe();
    }
}
