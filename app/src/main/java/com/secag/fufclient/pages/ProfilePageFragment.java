package com.secag.fufclient.pages;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.secag.fufclient.PreferenceItem;
import com.secag.fufclient.R;

import javax.json.Json;
import javax.json.JsonArray;

public class ProfilePageFragment extends Fragment {

    FragmentActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnClickListenersPrefs();
        fillPrefsBlock();
    }

    public void changePage(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.preferences_edit:
                fragment = new InterestsPageFragment();
                break;
            case R.id.feed_preferences_edit:
                fragment = new BlockedInterestsPageFragment();
                break;
        }
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.pagesFragment, fragment);
        ft.commit();
    }

    public void togglePreferencesBlock(View view) {
        ViewGroup sceneRoot = (ViewGroup) activity.findViewById(R.id.preferences_root);
        Scene closed = Scene.getSceneForLayout(sceneRoot, R.layout.fragment_profile_preferences, activity);
        Scene opened = Scene.getSceneForLayout(sceneRoot, R.layout.fragment_profile_preferences_wide, activity);
        TransitionSet transition = new TransitionSet();
        transition.addTransition(new ChangeBounds());
        transition.addTransition(new Fade());
        transition.setOrdering(TransitionSet.ORDERING_SEQUENTIAL);
        ((ViewGroup) activity.findViewById(R.id.profile)).getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        if (view.getId() == R.id.open_pref_button) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(sceneRoot.getWidth(), Math.round(400 * getResources().getDisplayMetrics().density));
            sceneRoot.setLayoutParams(layoutParams);
            TransitionManager.go(opened, transition);
            fillPrefsBlock();
            fillLocationsBlock();
            fillBlockedPrefsBlock();
            setOnClickListenersPrefsWide();
        } else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(sceneRoot.getWidth(), Math.round(250 * getResources().getDisplayMetrics().density));
            layoutParams.setMargins(0, 0, 0, Math.round(20 * getResources().getDisplayMetrics().density));
            sceneRoot.setLayoutParams(layoutParams);
            TransitionManager.go(closed, transition);
            fillPrefsBlock();
            setOnClickListenersPrefs();
        }
    }

    void fillPrefsBlock() {
        ViewGroup block = (ViewGroup) activity.findViewById(R.id.preferences_pref_block);
        if (block == null) {
            return;
        }
        JsonArray array = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("id", 0)
                        .add("emoji", "💻")
                        .add("title", "IT"))
                .add(Json.createObjectBuilder()
                        .add("id", 1)
                        .add("emoji", "🍺")
                        .add("title", "beer"))
                .add(Json.createObjectBuilder()
                        .add("id", 2)
                        .add("emoji", "\uD83C\uDDFA\uD83C\uDDE6")
                        .add("title", "Ukraine"))
                .add(Json.createObjectBuilder()
                        .add("id", 3)
                        .add("emoji", "🎺")
                        .add("title", "trumpets"))
                .add(Json.createObjectBuilder()
                        .add("id", 4)
                        .add("emoji", "\uD83E\uDD95")
                        .add("title", "paleontology")).build();
        for (int i = 0; i < array.size(); i++) {
            block.addView(new PreferenceItem(
                    activity,
                    null,
                    PreferenceItem.ItemType.PREFERENCE,
                    array.getJsonObject(i).getInt("id"),
                    array.getJsonObject(i).getString("emoji"),
                    array.getJsonObject(i).getString("title")));
        }
    }

    void fillLocationsBlock() {
        ViewGroup block = (ViewGroup) activity.findViewById(R.id.preferences_locations_block);
        if (block == null) {
            return;
        }
        JsonArray array = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("id", 0)
                        .add("title", "NURE"))
                .add(Json.createObjectBuilder()
                        .add("id", 1)
                        .add("title", "Molodist restourant, Kharkiv"))
                .add(Json.createObjectBuilder()
                        .add("id", 2)
                        .add("title", "Sweeter")).build();
        for (int i = 0; i < array.size(); i++) {
            block.addView(new PreferenceItem(
                    activity,
                    null,
                    PreferenceItem.ItemType.LOCATION,
                    array.getJsonObject(i).getInt("id"),
                    "",
                    array.getJsonObject(i).getString("title")));
        }
    }

    void fillBlockedPrefsBlock() {
        ViewGroup block = (ViewGroup) activity.findViewById(R.id.preferences_feed_prefs_block);
        if (block == null) {
            return;
        }
        JsonArray array = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("id", 0)
                        .add("emoji", "\uD83C\uDDF7\uD83C\uDDFA")
                        .add("title", "russia")).build();
        for (int i = 0; i < array.size(); i++) {
            PreferenceItem preferenceItem = new PreferenceItem(
                    activity,
                    null,
                    PreferenceItem.ItemType.BLOCKED_PREFERENCE,
                    array.getJsonObject(i).getInt("id"),
                    array.getJsonObject(i).getString("emoji"),
                    array.getJsonObject(i).getString("title"));
            preferenceItem.toggleChecked();
            block.addView(preferenceItem);
        }
    }

    void setOnClickListenersPrefs() {
        ImageButton openPrefs = activity.findViewById(R.id.open_pref_button);
        openPrefs.setOnClickListener(this::togglePreferencesBlock);
    }

    void setOnClickListenersPrefsWide() {
        ImageButton openPrefs = activity.findViewById(R.id.preferences_edit);
        openPrefs.setOnClickListener(this::changePage);
        openPrefs = activity.findViewById(R.id.feed_preferences_edit);
        openPrefs.setOnClickListener(this::changePage);
        openPrefs = activity.findViewById(R.id.close_pref_button);
        openPrefs.setOnClickListener(this::togglePreferencesBlock);
    }
}