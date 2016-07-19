# PodSlider

----
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-PodSLider-green.svg?style=true)](https://android-arsenal.com/details/1/3836)
[![Release](https://jitpack.io/v/bhargavms/PodSLider.svg)](https://jitpack.io/bhargavms/PodSLider)

#### Sample:

![Sample Gif](assets/gifs/ezgif.com-gif-maker.gif?raw=true)
![Sample Gif](assets/gifs/view_pager_interaction?raw=true)

## A slider view.

> This project is an attempt to port the svg pod slider created by Chris Gannon to android.
> see [CodePen](http://codepen.io/chrisgannon/pen/mPoMxq)

#### version 1.2.0 Update:
 - Major enhancements in how animations are done.
 - Added a ease out elastic effect(a wobble effect just before the circles stop).

#### Gradle:

Add it to your project build.gradle with:

```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
and to your module's build.gradle dependencies block add the dependency to the library:
> P.S Check Jitpack badge at the top of the readme for the latest version name.

```gradle
dependencies {
    compile 'com.github.bhargavms:PodSLider:X.X.X'
}
```

## Usage
#### Xml Attributes
```xml
app:mainSliderColor="@color/mainPodSlider" <!-- the color of the main rounded rectangular bar. -->
app:numberOfPods="2" <!-- the number of small circles (i.e pods) in the slider.-->
app:podColor="#4CAF50" <!-- the color of the pod when its not selected.-->
app:selectedPodColor="#fff" <!-- the color of the pod when its selected.-->
```
#### Setting a click listener
```java
PodSlider podSlider = (PodSlider) findViewById(R.id.pod_slider);
podSlider.setPodClickListener(new OnPodClickListener() {
    @Override
    public void onPodClick(int position) {
        Log.d("PodPosition", "position = " + position);
    }
});
```

#### To set Currently selected Pod
```java
podSlider.setCurrentlySelectedPod(1);
// if you want to change selected pod and also animate
// then use the below method, But please remember to use this only after
// the view has been rendered (i.e donot use this in onCreate()/onCreateView()
// this will cause the large and medium circle to be drawn at (0, height/2)
podSlider.setcurrentlySelectedPodAndAnimate(1);
```

#### To set up with a ViewPager
> By default, the widget uses the String returned by the viewpager 
> adapter's `getPageTitle(int)` method, to disable it just set
> `pager.usePageTitle(false)` as false, **before setting up with viewPager** 

```java
ViewPager pager = (ViewPager) findViewById(R.id.pager);
PodSlider pagerSlider = (PodSlider) findViewById(R.id.pager_slider);
PodPagerAdapter adapter = new PodPagerAdapter(getSupportFragmentManager());
pager.setAdapter(adapter);
pagerSlider.setUpWithViewPager(pager);
```

#### To change the number of pods programatically
```java
podSlider.setNumberOfPods(4);
```

#### To set custom text 

```
// Use this after setting number of pods and make sure the length of texts 
// array is equal to or greater than `numberOfPods` that you set using `setNumberOfPods()`
podSlider.setPodTexts(new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"});
```

> (ideally use single character don't ever have texts with more than 1 char looks ugly)

#### To set Drawables

```
Drawable access = getResources().getDrawable(R.drawable.ic_accessibility_black_24dp);
Drawable account = getResources().getDrawable(R.drawable.ic_account_circle_black_24dp);
Drawable car = getResources().getDrawable(R.drawable.ic_directions_car_black_24dp);
// Use this after setting number of pods and make sure the length 
// of drawables array is equal to or greater than `numberOfPods`
// that you set using `setNumberOfPods()`
podSlider.setPodDrawables(
        new Drawable[]{
                access, account, car,
                access, account, car,
                access, account, car,
                access
        }, PodSlider.DrawableSize.FIT_POD_CIRCLE
);
```

> I have not added (and do not plan to in the near future) support for passing in 
> drawable resource Ids, loading the drawable from resource/creating it from bitmaps
> or whatever is left to you, if you want further customization on deciding the
> size of the drawable please open an issue, based on the amount of traction it receives
> I might decide to implement it.
 
 
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
