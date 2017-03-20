package ravtrix.foodybuddy.fragments.inbox;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.fragments.friendsfrag.FriendsFrag;

/**
 * Created by Ravinder on 3/10/17.
 */

public class InboxTabsFragment extends android.support.v4.app.Fragment {

    @BindView(R.id.frag_inbox_tabs_tabLayout) protected TabLayout tabLayout;
    @BindView(R.id.frag_inbox_tabs_viewpager) protected CustomViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_inbox_tabs, container, false);

        ButterKnife.bind(this, view);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPagerAdapter.addFragments(new InboxFragment(), "Group Chat");
        viewPagerAdapter.addFragments(new FriendsFrag(), "Private Chat");

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setEnableSwipe(false);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<android.support.v4.app.Fragment> fragmentList = new ArrayList<>();
        private ArrayList<String> titleList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void addFragments(android.support.v4.app.Fragment fragment, String title) {
            this.fragmentList.add(fragment);
            this.titleList.add(title);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }


    }
}
