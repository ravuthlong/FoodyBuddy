package ravtrix.foodybuddy.fragments.friendsfrag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ravtrix.foodybuddy.R;

/**
 * Created by Ravinder on 1/27/17.
 */

public class FriendsFrag extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_friends, container, false);
    }
}
