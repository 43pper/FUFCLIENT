package com.secag.fufclient;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.secag.fufclient.pages.FeedPageFragment;
import com.secag.fufclient.pages.MessagesPageFragment;
import com.secag.fufclient.pages.ProfilePageFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void changePage(View view) {
        Fragment fragment = null;
        switch (view.getId()) {
            case R.id.feedButton:
                fragment = new FeedPageFragment();
                break;
            case R.id.messagesButton:
                fragment = new MessagesPageFragment();
                break;
            case R.id.profileButton:
                fragment = new ProfilePageFragment();
                break;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.pagesFragment, fragment);
        ft.commit();
        fm = getSupportFragmentManager();
//        System.out.printf(getSupportFragmentManager().getFragments().get(0).toString());
        int frames = getSupportFragmentManager().getFragments().size();
        System.out.println(frames);
    }
}
