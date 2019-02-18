package com.kitkat.group.clubs;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Glenn on 13/02/2019.
 */

public class UserFragment extends Fragment {

    private static final String TAG = "UserFragment";

    public UserFragment() {
        // Empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started UserFragment");

        View view = inflater.inflate(R.layout.fragment_user, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPagerId);
        setupViewPager(viewPager);

        TabLayout tabs = (TabLayout) view.findViewById(R.id.tabsId);
        tabs.setupWithViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ActivityFragment(), "Activity");
        adapter.addFragment(new SettingsFragment(), "Settings");
        viewPager.setAdapter(adapter);
    }
}
