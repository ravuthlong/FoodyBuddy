package ravtrix.foodybuddy.fragments.userprofilefrag.subfrags;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.activities.editprofileimage.EditProfileImageActivity;
import ravtrix.foodybuddy.activities.editprofileimage.model.ProfileImageModel;
import ravtrix.foodybuddy.localstore.UserLocalStore;
import ravtrix.foodybuddy.utils.Helpers;
import ravtrix.foodybuddy.utils.RetrofitUserInfoSingleton;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Ravinder on 1/30/17.
 */

public class UserProfileHeadOneFrag extends Fragment implements View.OnClickListener {

    @BindView(R.id.frag_userprofile_background) protected ImageView backgroundImage;
    @BindView(R.id.frag_userprofile_profileImage) protected CircleImageView profileImage;
    @BindView(R.id.frag_userprofile_username) protected TextView tvUsername;
    @BindView(R.id.frag_profile_head1_mainRelative) protected RelativeLayout mainRelative;
    @BindView(R.id.frag_profile_head1_bSetting) protected ImageView bSetting;
    private UserLocalStore userLocalStore;
    private CompositeSubscription mSubscriptions;
    public static final String CLASS_NAME = UserProfileHeadOneFrag.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_profile_head1, container, false);
        ButterKnife.bind(this, view);

        Helpers.overrideFonts(getContext(), mainRelative);
        Helpers.overrideFonts(getContext(), tvUsername);

        bSetting.setOnClickListener(this);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        userLocalStore = new UserLocalStore(getActivity());
        mSubscriptions = new CompositeSubscription();

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frag_profile_head1_bSetting:

                startActivity(new Intent(getActivity(), EditProfileImageActivity.class));
                break;
        }
    }

    public void loadViewWithData() {

        mSubscriptions.add(RetrofitUserInfoSingleton.getRetrofitUserInfo()
                    .getAUserPhoto()
                    .getAUserPhoto(userLocalStore.getLoggedInUser().getUserID())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<ProfileImageModel>() {
                        @Override
                        public void onCompleted() {
                            Log.d(CLASS_NAME, "Fetch profile image completed");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(CLASS_NAME, "Error fetching image");
                        }

                        @Override
                        public void onNext(ProfileImageModel profileImageModel) {
                            if(profileImage != null) {
                                Picasso.with(getContext())
                                        .load(profileImageModel.getUrl())
                                        .fit()
                                        .centerCrop()
                                        .into(profileImage);
                            }

                            if(backgroundImage != null) {
                                Picasso.with(getContext())
                                        .load(profileImageModel.getUrl())
                                        .transform(new BlurTransformation(getContext()))
                                        .fit()
                                        .centerCrop()
                                        .into(backgroundImage);
                            }
                        }
                    }));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
