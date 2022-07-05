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
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.secag.fufclient.pages.BlockedInterestsPageFragment;
import com.secag.fufclient.pages.FeedPageFragment;
import com.secag.fufclient.pages.InterestsPageFragment;
import com.secag.fufclient.pages.MessagesPageFragment;
import com.secag.fufclient.pages.ProfilePageFragment;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((BottomNavigationView) findViewById(R.id.bottom_navigation)).setSelectedItemId(R.id.profile_nav);
        ((BottomNavigationView) findViewById(R.id.bottom_navigation)).setOnItemSelectedListener(item -> {
            FragmentContainerView view = findViewById(R.id.pagesFragment);
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.messages_nav:
                    fragment = new MessagesPageFragment();
                    break;
                case R.id.feed_nav:
                    fragment = new FeedPageFragment();
                    break;
                default:
                    fragment = new ProfilePageFragment();
            }
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.pagesFragment, fragment);
            ft.commit();
            return true;
        });
    }
}
