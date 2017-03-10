package ravtrix.foodybuddy.fragments.userprofilefrag.subfrags;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import ravtrix.foodybuddy.activities.login.LoginActivity;
import ravtrix.foodybuddy.localstore.UserLocalStore;
import ravtrix.foodybuddy.utils.Helpers;

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
        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frag_profile_head1_bSetting:
                userLocalStore.clearUserData();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                break;
        }
    }

    public void loadViewWithData() {

        if(profileImage != null) {
            Picasso.with(getContext())
                    .load("http://media.safebee.com/assets/images/2015/12/guy%20with%20giant%20burger.jpg.838x0_q67_crop-smart.jpg")
                    .fit()
                    .centerCrop()
                    .into(profileImage);
        }

        if(backgroundImage != null) {
            Picasso.with(getContext())
                    .load("http://media.safebee.com/assets/images/2015/12/guy%20with%20giant%20burger.jpg.838x0_q67_crop-smart.jpg")
                    .transform(new BlurTransformation(getContext()))
                    .fit()
                    .centerCrop()
                    .into(backgroundImage);
        }
    }

}
