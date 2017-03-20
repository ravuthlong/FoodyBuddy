package ravtrix.foodybuddy.fragments.userprofilefrag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.fragments.activityprofilefrag.ActivityProfileFrag;
import ravtrix.foodybuddy.fragments.eventprofilefrag.EventProfileFrag;
import ravtrix.foodybuddy.fragments.userprofilefrag.subfrags.UserProfileHeadOneFrag;
import ravtrix.foodybuddy.fragments.userprofilefrag.subfrags.UserProfileHeadTwoFrag;
import ravtrix.foodybuddy.utils.Helpers;

/**
 * Created by Ravinder on 1/27/17.
 */

public class UserProfileFrag extends Fragment {

    @BindView(R.id.frag_userprofile_viewpager) protected ViewPager viewPager;

    @BindView(R.id.frag_userprofile_indicator) protected CircleIndicator circleIndicator;
    @BindView(R.id.frag_userprofile_linearFollowers) protected LinearLayout linearFollowers;
    @BindView(R.id.frag_userprofile_linearFollowing) protected LinearLayout linearFollowing;
    @BindView(R.id.frag_userprofile_linearJoined) protected LinearLayout linearJoined;
    @BindView(R.id.frag_userprofile_tabhost) protected FragmentTabHost tabHost;
    private UserProfileFrag.ViewPagerAdapter adapter;
    boolean isFragLoaded = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && !isFragLoaded) {
            ((UserProfileHeadOneFrag) adapter.getFragmentAtPosition(0)).loadViewWithData();
            isFragLoaded = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag_userprofile, container, false);
        ButterKnife.bind(this, view);
        tabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        tabHost.addTab(tabHost.newTabSpec("eventFrag").setIndicator("", ContextCompat.getDrawable(getContext(), R.drawable.ic_grid)),
                EventProfileFrag.class, null);
        tabHost.addTab(tabHost.newTabSpec("activityFrag").setIndicator("", ContextCompat.getDrawable(getContext(), R.drawable.ic_menu_gray)),
                ActivityProfileFrag.class, null);

        Helpers.overrideFonts(getContext(), linearFollowers);
        Helpers.overrideFonts(getContext(), linearFollowing);
        Helpers.overrideFonts(getContext(), linearJoined);

        setupViewPager(viewPager);
        //viewPager.setOffscreenPageLimit(1); // define size 0,1

        return view;
    }


    /**
     * Set up the tab adapter with fragments
     * @param viewPager                 - the viewpager
     */
    private void setupViewPager(ViewPager viewPager) {

        adapter = new UserProfileFrag.ViewPagerAdapter(getFragmentManager());
        // Set adapter with different fragments and their titles
        adapter.addFragment(new UserProfileHeadOneFrag());
        adapter.addFragment(new UserProfileHeadTwoFrag());
        viewPager.setAdapter(adapter);
        circleIndicator.setViewPager(viewPager);
    }

    /**
     * Adapter class for custom tabs
     */
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>(); // array list with tab fragments

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        Fragment getFragmentAtPosition(int position) {
            return mFragmentList.get(position);
        }
    }
}
