package com.secag.fufclient;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.secag.fufclient.pages.InterestsPageFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fillPrefsBlock();
    }

    public void changePage(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.preferences_edit:
                fragment = new InterestsPageFragment();
                break;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.pagesFragment, fragment);
        ft.commit();
    }

    public void togglePreferencesBlock(View view) {
        ViewGroup sceneRoot = (ViewGroup) findViewById(R.id.preferences_root);
        Scene closed = Scene.getSceneForLayout(sceneRoot, R.layout.fragment_profile_preferences, this);
        Scene opened = Scene.getSceneForLayout(sceneRoot, R.layout.fragment_profile_preferences_wide, this);
        TransitionSet transition = new TransitionSet();
        transition.addTransition(new ChangeBounds());
        transition.addTransition(new Fade());
        transition.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
        ((ViewGroup) findViewById(R.id.profile)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        if (view.getId() == R.id.open_pref_button) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(sceneRoot.getWidth(), Math.round(400 * getResources().getDisplayMetrics().density));
            sceneRoot.setLayoutParams(layoutParams);
            TransitionManager.go(opened, transition);
            fillPrefsBlock();
            fillLocationsBlock();
            fillBlockedPrefsBlock();
        } else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(sceneRoot.getWidth(), Math.round(250 * getResources().getDisplayMetrics().density));
            layoutParams.setMargins(0, 0, 0, Math.round(20 * getResources().getDisplayMetrics().density));
            sceneRoot.setLayoutParams(layoutParams);
            TransitionManager.go(closed, transition);
            fillPrefsBlock();
        }
    }

    void fillPrefsBlock() {
        ViewGroup block = (ViewGroup) findViewById(R.id.preferences_pref_block);
        if (block == null) {
            return;
        }
        String[][] string = {{"💻", "IT"}, {"🍺", "beer"}, {"\uD83C\uDDFA\uD83C\uDDE6", "Ukraine"}, {"🎺", "trumpets"}, {"\uD83E\uDD95", "paleontology"}};
        for (String[] item : string) {
            block.addView(new PreferenceItem(this, null, PreferenceItem.ItemType.PREFERENCE, item[0], item[1]));
        }
    }

    void fillLocationsBlock() {
        ViewGroup block = (ViewGroup) findViewById(R.id.preferences_locations_block);
        if (block == null) {
            return;
        }
        String[] string = {"NURE", "Molodist restourant, Kharkiv", "Sweeter"};
        for (String item : string) {
            block.addView(new PreferenceItem(this, null, PreferenceItem.ItemType.LOCATION, "", item));
        }
    }

    void fillBlockedPrefsBlock() {
        ViewGroup block = (ViewGroup) findViewById(R.id.preferences_feed_prefs_block);
        if (block == null) {
            return;
        }
        String[][] string = {{"\uD83C\uDDF7\uD83C\uDDFA", "russia"}};
        for (String[] item : string) {
            PreferenceItem preferenceItem = new PreferenceItem(this, null, PreferenceItem.ItemType.BLOCKED_PREFERENCE, item[0], item[1]);
            preferenceItem.toggleChecked();
            block.addView(preferenceItem);
        }
    }
}
