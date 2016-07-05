# PodSlider

----
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/7f7687dd718e43c9b3a06e8bdd055fe8)](https://www.codacy.com/app/bhargav521/PodSLider?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=bhargavms/PodSLider&amp;utm_campaign=Badge_Grade)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-PodSLider-green.svg?style=true)](https://android-arsenal.com/details/1/3836)

## A slider view designed by Chris Gannon
see [CodePen](http://codepen.io/chrisgannon/pen/mPoMxq)

> This project is an attempt to port the svg pod slider created by Chris Gannon to android.

Gradle:
Add it to your project build.gradle with:

```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
and to your module's build.gradle dependencies block add the dependency to the library:

```gradle
dependencies {
    compile 'com.github.bhargavms:PodSLider:1.1.2'
}
```

## Usage
#### Xml Attributes
```
app:mainSliderColor="@color/mainPodSlider" // the color of the main rounded rectangular bar.
app:numberOfPods="2" // the number of small circles (i.e pods) in the slider.
app:podColor="#4CAF50" // the color of the pod when its not selected.
app:selectedPodColor="#fff" // the color of the pod when its selected.
```
#### Setting a click listener
```
PodSlider podSlider = (PodSlider) findViewById(R.id.pod_slider);
podSlider.setPodClickListener(new OnPodClickListener() {
    @Override
    public void onPodClick(int position) {
        Log.d("PodPosition", "position = " + position);
    }
});
```

#### To set Currently selected Pod
```
podSlider.setCurrentlySelectedPod(1);
```

#### To set up with a ViewPager
```
ViewPager pager = (ViewPager) findViewById(R.id.pager);
PodSlider pagerSlider = (PodSlider) findViewById(R.id.pager_slider);
PodPagerAdapter adapter = new PodPagerAdapter(getSupportFragmentManager());
pager.setAdapter(adapter);
pagerSlider.setUpWithViewPager(pager);
```

#### To change the number of pods programatically
```
podSlider.setNumberOfPods(4);
```
#### Sample:

![Sample Gif](assets/gifs/ezgif.com-gif-maker.gif?raw=true)

### LICENSE: (Apache 2.0)
```
Copyright [2016] [Bhargav M S]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
