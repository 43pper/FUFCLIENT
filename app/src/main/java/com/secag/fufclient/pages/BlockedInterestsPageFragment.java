package com.secag.fufclient.pages;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.secag.fufclient.PreferenceItem;
import com.secag.fufclient.R;

import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;

public class BlockedInterestsPageFragment extends Fragment {

    ArrayList<Integer> selectedInterestsId = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blocked_interests_page, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.slide_right));
        setExitTransition(inflater.inflateTransition(R.transition.slide_right));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        ImageButton backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(view1 -> {
            Fragment fragment = new ProfilePageFragment();
            FragmentManager fm = activity.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.pagesFragment, fragment);
            ft.commit();
        });
        ViewGroup block = (ViewGroup) view.findViewById(R.id.blocked_interests_block);
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
        JsonArray selectedItems = Json.createArrayBuilder()
                .add(Json.createObjectBuilder()
                        .add("id", 0)
                        .add("emoji", "💻")
                        .add("title", "IT"))
                .add(Json.createObjectBuilder()
                        .add("id", 2)
                        .add("emoji", "\uD83C\uDDFA\uD83C\uDDE6")
                        .add("title", "Ukraine"))
                .add(Json.createObjectBuilder()
                        .add("id", 3)
                        .add("emoji", "🎺")
                        .add("title", "trumpets")).build();
        for (int i = 0; i < array.size(); i++) {
            PreferenceItem preferenceItem = new PreferenceItem(
                    activity,
                    null,
                    PreferenceItem.ItemType.BLOCKED_PREFERENCE,
                    array.getJsonObject(i).getInt("id"),
                    array.getJsonObject(i).getString("emoji"),
                    array.getJsonObject(i).getString("title"));
            if (selectedItems.stream().anyMatch(x -> x.asJsonObject().getInt("id") == preferenceItem.getDBId())) {
                selectedInterestsId.add(preferenceItem.getDBId());
                preferenceItem.toggleChecked();
            }
            preferenceItem.setOnClickListener(view1 -> {
                PreferenceItem item = (PreferenceItem) view1;
                if (item.toggleChecked()) {
                    selectedInterestsId.add(item.getDBId());
                } else {
                    selectedInterestsId.remove((Integer) item.getDBId());
                }
            });
            block.addView(preferenceItem);
        }
    }
}