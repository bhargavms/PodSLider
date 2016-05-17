package com.example.podslidersample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.podslider.OnPodClickListener;
import com.example.podslider.Pod;
import com.example.podslider.PodSlider;

public class MainActivity extends AppCompatActivity implements OnPodClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PodSlider podSlider = (PodSlider) findViewById(R.id.pod_slider);
        podSlider.setPodClickListener(this);
    }

    @Override
    public void onPodClick(Pod pod) {
        Log.d("PodClick", "pod with center = (" + pod.getCenterX() + "," + pod.getCenterY() + ")");
    }
}
