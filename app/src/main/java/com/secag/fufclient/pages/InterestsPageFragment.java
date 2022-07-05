package com.secag.fufclient.pages;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.secag.fufclient.PreferenceItem;
import com.secag.fufclient.R;

public class InterestsPageFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_interests_page, container, false);
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
        ViewGroup block = (ViewGroup) view.findViewById(R.id.interests_block);
        if (block == null) {
            return;
        }
        String[][] string = {{"💻", "IT"}, {"🍺", "beer"},
                {"\uD83C\uDDFA\uD83C\uDDE6", "Ukraine"},
                {"🎺", "trumpets"},
                {"\uD83E\uDD95", "paleontology"}};
        for (String[] item : string) {
            PreferenceItem preferenceItem = new PreferenceItem(activity, null, PreferenceItem.ItemType.PREFERENCE, item[0], item[1]);
            preferenceItem.setOnClickListener(view1 -> {
                ((PreferenceItem) view1).toggleChecked();
            });
            block.addView(preferenceItem);
        }
    }
}