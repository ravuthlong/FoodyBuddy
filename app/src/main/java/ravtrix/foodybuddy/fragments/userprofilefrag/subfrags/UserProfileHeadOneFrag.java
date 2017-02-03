package ravtrix.foodybuddy.fragments.userprofilefrag.subfrags;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.utils.Helpers;

/**
 * Created by Ravinder on 1/30/17.
 */

public class UserProfileHeadOneFrag extends Fragment {

    @BindView(R.id.frag_userprofile_background) protected ImageView backgroundImage;
    @BindView(R.id.frag_userprofile_profileImage) protected CircleImageView profileImage;
    @BindView(R.id.frag_profile_head1_mainRelative) protected RelativeLayout mainRelative;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_profile_head1, container, false);
        ButterKnife.bind(this, view);

        Helpers.overrideFonts(getContext(), mainRelative);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }

    public void loadViewWithData() {

        if(profileImage != null) {
            Picasso.with(getContext())
                    .load("http://b-i.forbesimg.com/zackomalleygreenburg/files/2014/01/0102_30-under-30-bruno-mars_650x455.jpg")
                    .fit()
                    .centerCrop()
                    .into(profileImage);
        }

        if(backgroundImage != null) {
            Picasso.with(getContext())
                    .load("http://b-i.forbesimg.com/zackomalleygreenburg/files/2014/01/0102_30-under-30-bruno-mars_650x455.jpg")
                    .transform(new BlurTransformation(getContext()))
                    .fit()
                    .centerCrop()
                    .into(backgroundImage);
        }
    }

}
