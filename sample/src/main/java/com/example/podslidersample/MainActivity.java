package com.example.podslidersample;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.bhargavms.podslider.OnPodClickListener;
import com.bhargavms.podslider.PodSlider;

public class MainActivity extends AppCompatActivity implements OnPodClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Drawable access = getResources().getDrawable(R.drawable.ic_accessibility_black_24dp);
        final Drawable account = getResources().getDrawable(R.drawable.ic_account_circle_black_24dp);
        final Drawable car = getResources().getDrawable(R.drawable.ic_directions_car_black_24dp);
        final PodSlider podSlider = (PodSlider) findViewById(R.id.pod_slider);
        assert podSlider != null;
        podSlider.setPodClickListener(new OnPodClickListener() {
            @Override
            public void onPodClick(int position) {
                Log.d("PodPosition", "position = " + position);
            }
        });
        podSlider.setCurrentlySelectedPod(1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                podSlider.setNumberOfPods(6);
            }
        }, 5000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                podSlider.setPodTexts(new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"});
            }
        }, 10000);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                podSlider.setPodDrawables(
//                        new Drawable[]{
//                                access, account, car,
//                                access, account, car,
//                                access, account, car,
//                                access
//                        }, PodSlider.DrawableSize.FIT_POD_CIRCLE
//                );
//            }
//        }, 15000);

//        ViewPager pager = (ViewPager) findViewById(R.id.pager);
//        PodSlider pagerSlider = (PodSlider) findViewById(R.id.pager_slider);
//        PodPagerAdapter adapter = new PodPagerAdapter(getSupportFragmentManager());
//        assert pager != null;
//        pager.setAdapter(adapter);
//        assert pagerSlider != null;
//        pagerSlider.setUpWithViewPager(pager);
////        int accentColor = getResources().getColor(R.color.colorAccent);
//        Drawable[] drawables = new Drawable[]{
//                access, account, car
//        };
//        pagerSlider.setPodDrawables(drawables, PodSlider.DrawableSize.FIT_MEDIUM_CIRCLE);
    }

    @Override
    public void onPodClick(int position) {

    }

    private class PodPagerAdapter extends FragmentStatePagerAdapter {
        public PodPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new BlankFragment();
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
