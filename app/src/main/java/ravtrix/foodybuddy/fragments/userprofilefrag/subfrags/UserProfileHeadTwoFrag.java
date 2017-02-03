package ravtrix.foodybuddy.fragments.userprofilefrag.subfrags;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.utils.Helpers;

/**
 * Created by Ravinder on 1/30/17.
 */

public class UserProfileHeadTwoFrag extends Fragment {

    @BindView(R.id.frag_profile_head2_tvDetails) protected TextView tvDetails;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_profile_head2, container, false);
        ButterKnife.bind(this, view);
        Helpers.overrideFonts(getContext(), tvDetails);

        return view;
    }
}
