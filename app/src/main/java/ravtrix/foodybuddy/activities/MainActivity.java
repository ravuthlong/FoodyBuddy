package ravtrix.foodybuddy.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ravtrix.foodybuddy.R;
import ravtrix.foodybuddy.fragments.friendsfrag.FriendsFrag;
import ravtrix.foodybuddy.fragments.maineventfrag.MainEventFrag;
import ravtrix.foodybuddy.fragments.userprofilefrag.UserProfileFrag;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.viewpager) protected ViewPager viewPager;
    @BindView(R.id.tabs) protected TabLayout tabLayout;
    @BindView(R.id.toolbar) protected Toolbar toolbar;
    @BindView(R.id.activity_main_drawer_layout) protected DrawerLayout drawerLayout;
    @BindView(R.id.acitivty_main_nav_view) protected NavigationView navigationView;
    private ImageView imageSetting, imageNavigation;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Set views
        TextView toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        imageNavigation = (ImageView) toolbar.findViewById(R.id.layout_main_imageNavigation);
        imageSetting = (ImageView) toolbar.findViewById(R.id.layout_main_imageSetting);

        // Set font
        toolbarTitle.setTypeface(Typeface.createFromAsset(getAssets(), "toolbar2.ttf"));

        setupViewPager(viewPager); // set adapter with data
        tabLayout.setupWithViewPager(viewPager); // push viewpager into the tab layout

        // Set up tabs and view-pager
        setTabIcons();
        setTabLayoutListener();
        setViewPagerListener();

        // set button listeners
        imageNavigation.setOnClickListener(this);
        imageSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_main_imageNavigation:
                drawerLayout.openDrawer(navigationView);
                break;
            case R.id.layout_main_imageSetting:
                break;
            default:
                break;
        }
    }

    private void lockDrawerLayoutScroll() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); // lock from swipe
    }

    private void hideMainToolbarButtons() {
        imageNavigation.setVisibility(View.INVISIBLE);
        imageSetting.setVisibility(View.INVISIBLE);
    }

    private void showMainToolbarButtons() {
        imageNavigation.setVisibility(View.VISIBLE);
        imageSetting.setVisibility(View.VISIBLE);
    }

    /**
     * Set up the tab adapter with fragments
     * @param viewPager                 - the viewpager
     */
    private void setupViewPager(ViewPager viewPager) {

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        // Set adapter with different fragments and their titles
        adapter.addFragment(new MainEventFrag());
        adapter.addFragment(new UserProfileFrag()); // inbox
        adapter.addFragment(new UserProfileFrag());
        adapter.addFragment(new FriendsFrag());
        viewPager.setAdapter(adapter);
    }

    /**
     * Set up icons for different tabs
     */
    private void setTabIcons() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            switch (i) {
                case 0:
                    tabLayout.getTabAt(i).setIcon(R.drawable.ic_home);
                    break;
                case 1:
                    tabLayout.getTabAt(i).setIcon(R.drawable.ic_inbox);
                    break;
                case 2:
                    tabLayout.getTabAt(i).setIcon(R.drawable.ic_user);
                    break;
                case 3:
                    tabLayout.getTabAt(i).setIcon(R.drawable.ic_user_friends);
                    break;
                default:
                    tabLayout.getTabAt(i).setIcon(R.drawable.ic_user_friends);
                    break;
            }
        }
    }

    /**
     * Listens for when the user "clicks" between tabs or re-clicks the tabs
     */
    private void setTabLayoutListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {}
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // This is when the user re-clicks the main tab button. This should scroll the recycler view
                // back to the top
                if (tab.getPosition() == 0) {
                    ((MainEventFrag) adapter.getFragmentAtPosition(0)).scrollToTop();
                }
            }
        });
    }

    /**
     * Listens for when the user scrolls or clicks between tabs
     */
    private void setViewPagerListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        showMainToolbarButtons();
                        break;
                    case 1:
                    case 2:
                    case 3:
                        lockDrawerLayoutScroll();
                        hideMainToolbarButtons();
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
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
