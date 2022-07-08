package com.secag.fufclient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.secag.fufclient.pages.FeedCardFragment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedCardAdapter extends FragmentStateAdapter {
    RecyclerView rv;
    ArrayList<User> cards = new ArrayList<>();

    public FeedCardAdapter(@NonNull Fragment fragment) {
        super(fragment);
        updateFeed();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position >= cards.size()) {
            return new FeedCardFragment();
        }
        int id = cards.get(position).getId();
        String username = cards.get(position).getName() + " " + cards.get(position).getLastName();
        String description = cards.get(position).getProfileDescription();
        ArrayList<Interest> interests = cards.get(position).prefs;
        List<Location> locationList = cards.get(position).favouriteLocations;
        locationList.sort(Comparator.comparingInt(Location::getRating));
        if (locationList.size() == 0) {
            return new FeedCardFragment(id, username, interests, new String[]{}, description);
        }
        String[] locations;
        if (locationList.size() == 1) {
            locations = new String[]{locationList.get(0).getTitle()};
        } else {
            locations = new String[]{locationList.get(locationList.size() - 1).getTitle(), locationList.get(locationList.size() - 2).getTitle()};
        }
        return new FeedCardFragment(id, username, interests, locations, description);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        rv = recyclerView;
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    void updateFeed() {
        App.getFufApi().getFeedRecommendations(1, 29).enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (response.body() == null) {
                    return;
                }
                cards.addAll(response.body());
                for (int i = 0; i < cards.size(); i++) {
                    int finalI = i;
                    App.getFufApi().getInterestsByUserId(cards.get(i).getId()).enqueue(new Callback<ArrayList<Interest>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Interest>> call, Response<ArrayList<Interest>> response) {
                            if (response.body() != null) {
                                cards.get(finalI).prefs = response.body();
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Interest>> call, Throwable t) {

                        }
                    });
                }
                notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {

            }
        });
    }

    @Override
    public long getItemId(int position) {
        return (long) cards.get(position).hashCode() + position;
    }

    @Override
    public boolean containsItem(long itemId) {
        for (int i = 0; i < cards.size(); i++) {
            if ((long) cards.get(i).hashCode() + i == itemId) {
                return true;
            }
        }
        return false;
    }
}
