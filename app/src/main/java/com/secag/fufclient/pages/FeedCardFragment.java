package com.secag.fufclient.pages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.secag.fufclient.Interest;
import com.secag.fufclient.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FeedCardFragment extends Fragment {

    int id;
    String username;
    String description;
    ArrayList<Interest> prefs;
    String[] locations;

    public FeedCardFragment() {

    }

    public FeedCardFragment(int id, String username, ArrayList<Interest> prefs, String[] locations, String description) {
        this.id = id;
        this.prefs = prefs;
        this.username = username;
        this.description = description;
        this.locations = locations;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (username == null) {
            return;
        }
        ((TextView) view.findViewById(R.id.feed_username)).setText(username);
        ((TextView) view.findViewById(R.id.feed_desc_text)).setText(description);

        int[][] viewIds = new int[][] {{R.id.feed_pref_emoji1, R.id.feed_pref_text1, R.id.first_preference},
                {R.id.feed_pref_emoji2, R.id.feed_pref_text2, R.id.second_preference},
                {R.id.feed_pref_emoji3, R.id.feed_pref_text3, R.id.third_preference},
                {R.id.feed_pref_emoji4, R.id.feed_pref_text4, R.id.fourth_preference}};
        for (int i = 0; i < 4; i++) {
            if (prefs.size() > i) {
                ((TextView) view.findViewById(viewIds[i][0])).setText(prefs.get(i).getEmoji());
                if (prefs.get(i).getTitle().length() > 10) {
                    prefs.get(i).setTitle(prefs.get(i).getTitle().substring(0, 8) + "..");
                }
                ((TextView) view.findViewById(viewIds[i][1])).setText(prefs.get(i).getTitle());
            } else {
                view.findViewById(viewIds[i][2]).setVisibility(View.INVISIBLE);
            }
        }
        if (locations.length > 0) {
            ((TextView) view.findViewById(R.id.feed_loc_text1)).setText(locations[0]);
        } else {
            view.findViewById(R.id.first_location).setVisibility(View.INVISIBLE);
        }
        if (locations.length > 1) {
            ((TextView) view.findViewById(R.id.feed_loc_text2)).setText(locations[1]);
        } else {
            view.findViewById(R.id.second_location).setVisibility(View.INVISIBLE);
        }
        ImageView imageView = view.findViewById(R.id.feed_profile_pic);
        Picasso.with(getActivity()).load("https://fuf.azurewebsites.net/profiles/" + id + "/photo").into(imageView);
    }
}