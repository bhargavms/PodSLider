package com.example.podslidersample;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bhargavms.podslider.OnPodClickListener;
import com.bhargavms.podslider.PodSlider;

import java.util.ArrayList;
import java.util.List;

public class PagerInteractionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_interaction);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        final PodSlider pagerSlider = (PodSlider) findViewById(R.id.pager_slider);
        List<Fragment> fragments = new ArrayList<>(2);
        fragments.add(new BlankFragment());
        fragments.add(new BlankFragmentTwo());
        List<String> titles = new ArrayList<>(2);
        titles.add("A");
        titles.add("B");
//        final int[] colors = new int[]{
//                Color.parseColor("#FFC169"),
//                Color.parseColor("#F68C6F")
//        };
        assert pager != null;
        pager.setAdapter(new PagerAdapter(getSupportFragmentManager(), fragments, titles));
        assert pagerSlider != null;
        pagerSlider.setUpWithViewPager(pager);
//        pagerSlider.setPodClickListener(new OnPodClickListener() {
//            @Override
//            public void onPodClick(int position) {
//                pagerSlider.setBackgroundColor(colors[position]);
//            }
//        });
    }

    private static class PagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> fragments;
        private List<String> titles;

        public PagerAdapter(FragmentManager fm,
                            List<Fragment> fragments, List<String> titles) {
            super(fm);
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

}
