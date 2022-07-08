package com.secag.fufclient.pages;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        view.findViewById(R.id.done_button).setOnClickListener(view1 -> {
            FragmentManager manager = activity.getSupportFragmentManager();
            BlockedInterestsDialog interestsDialog = new BlockedInterestsDialog(selectedInterestsId);
            interestsDialog.show(manager, "myDialog");
        });
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
        final ArrayList<Interest>[] allInterests = new ArrayList[]{new ArrayList<>()};
        final ArrayList<Interest>[] userInterests = new ArrayList[]{new ArrayList<>()};
        App.getFufApi().getAllInterests().enqueue(new Callback<ArrayList<Interest>>() {

            @Override
            public void onResponse(Call<ArrayList<Interest>> call, Response<ArrayList<Interest>> response) {
                if (response.body() != null) {
                    allInterests[0] = response.body();
                    App.getFufApi().getBlockedInterestsByUserId(1).enqueue(new Callback<ArrayList<Interest>>() {

                        @Override
                        public void onResponse(Call<ArrayList<Interest>> call, Response<ArrayList<Interest>> response) {
                            if (response.body() != null) {
                                userInterests[0] = response.body();
                                for (int i = 0; i < allInterests[0].size(); i++) {
                                    PreferenceItem preferenceItem = new PreferenceItem(
                                            activity,
                                            null,
                                            PreferenceItem.ItemType.BLOCKED_PREFERENCE,
                                            allInterests[0].get(i).getId(),
                                            allInterests[0].get(i).getEmoji(),
                                            allInterests[0].get(i).getTitle());
                                    int finalI = i;
                                    if (userInterests[0].stream().anyMatch(item -> item.getId() == allInterests[0].get(finalI).getId())) {
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

                        @Override
                        public void onFailure(Call<ArrayList<Interest>> call, Throwable t) {
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Interest>> call, Throwable t) {
            }
        });
    }
}