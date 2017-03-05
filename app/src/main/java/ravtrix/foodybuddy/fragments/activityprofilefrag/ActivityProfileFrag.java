package ravtrix.foodybuddy.fragments.activityprofilefrag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import ravtrix.foodybuddy.R;

public class ActivityProfileFrag extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_activity_profile, container, false);
        ButterKnife.bind(this, view);

        return view;
    }
}
