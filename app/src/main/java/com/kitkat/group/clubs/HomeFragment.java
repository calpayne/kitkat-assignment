package com.kitkat.group.clubs;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Glenn on 13/02/2019.
 */

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    public HomeFragment() {
        // Empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started HomeFragment");

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPagerId);
        setupViewPager(viewPager);

        TabLayout tabs = (TabLayout) view.findViewById(R.id.tabsId);
        tabs.setupWithViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new NotificationFragment(), "Notifications");
        adapter.addFragment(new RecommendFragment(), "Recommended");
        viewPager.setAdapter(adapter);
    }
}
