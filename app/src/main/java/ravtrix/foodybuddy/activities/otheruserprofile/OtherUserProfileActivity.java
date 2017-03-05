package ravtrix.foodybuddy.activities.otheruserprofile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.activities.otheruserprofile.subfrags.ProfileHead1Frag;
import ravtrix.foodybuddy.utils.Helpers;

public class OtherUserProfileActivity extends AppCompatActivity {

    @BindView(R.id.activity_userprofile_viewpager) protected ViewPager viewPager;
    @BindView(R.id.activity_userprofile_linearShortcuts) protected LinearLayout linearShortcuts;
    private OtherUserProfileActivity.ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_user_profile);
        ButterKnife.bind(this);

        Helpers.overrideFonts(this, linearShortcuts);
        setupViewPager(viewPager);
    }


    /**
     * Set up the tab adapter with fragments
     * @param viewPager                 - the viewpager
     */
    private void setupViewPager(ViewPager viewPager) {

        adapter = new OtherUserProfileActivity.ViewPagerAdapter(getSupportFragmentManager());
        // Set adapter with different fragments and their titles
        adapter.addFragment(new ProfileHead1Frag());
        viewPager.setAdapter(adapter);
    }

    /**
     * Adapter class for custom tabs
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
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
