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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.secag.fufclient.App;
import com.secag.fufclient.Interest;
import com.secag.fufclient.PreferenceItem;
import com.secag.fufclient.R;
import com.secag.fufclient.User;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        TextView username = view.findViewById(R.id.profile_username);
        ImageView photo = view.findViewById(R.id.photoProfile);
        TextView description = view.findViewById(R.id.profile_description);
        Picasso.with(getActivity()).load("https://fuf.azurewebsites.net/profiles/1/photo").into(photo);
        App.getFufApi().getProfileById(1).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() == null) {
                    return;
                }
                username.setText(response.body().getName() + " " + response.body().getLastName());
                description.setText(response.body().getProfileDescription());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
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
        final ArrayList<Interest>[] array = new ArrayList[]{new ArrayList<>()};
        App.getFufApi().getInterestsByUserId(1).enqueue(new Callback<ArrayList<Interest>>() {
            @Override
            public void onResponse(Call<ArrayList<Interest>> call, Response<ArrayList<Interest>> response) {
                if (response.body() != null) {
                    array[0] = response.body();
                    for (int i = 0; i < array[0].size(); i++) {
                        block.addView(new PreferenceItem(
                                activity,
                                null,
                                PreferenceItem.ItemType.PREFERENCE,
                                array[0].get(i).getId(),
                                array[0].get(i).getEmoji(),
                                array[0].get(i).getTitle()));
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Interest>> call, Throwable t) {
                Toast.makeText(getActivity(), "An error occurred during networking", Toast.LENGTH_SHORT).show();
            }
        });
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
        final ArrayList<Interest>[] array = new ArrayList[]{new ArrayList<>()};
        App.getFufApi().getBlockedInterestsByUserId(1).enqueue(new Callback<ArrayList<Interest>>() {
            @Override
            public void onResponse(Call<ArrayList<Interest>> call, Response<ArrayList<Interest>> response) {
                if (response.body() != null) {
                    array[0] = response.body();
                    for (int i = 0; i < array[0].size(); i++) {
                        PreferenceItem preferenceItem = new PreferenceItem(
                                activity,
                                null,
                                PreferenceItem.ItemType.BLOCKED_PREFERENCE,
                                array[0].get(i).getId(),
                                array[0].get(i).getEmoji(),
                                array[0].get(i).getTitle());
                        preferenceItem.toggleChecked();
                        block.addView(preferenceItem);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Interest>> call, Throwable t) {
                Toast.makeText(getActivity(), "An error occurred during networking", Toast.LENGTH_SHORT).show();
            }
        });
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