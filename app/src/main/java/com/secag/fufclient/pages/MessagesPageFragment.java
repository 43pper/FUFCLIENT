package com.secag.fufclient.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.secag.fufclient.R;

import java.util.Objects;

import javax.json.Json;
import javax.json.JsonArray;

public class MessagesPageFragment extends Fragment {

    String type;

    public MessagesPageFragment(String type) {
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (Objects.equals(type, "chats")) {
            return inflater.inflate(R.layout.fragment_messages_page, container, false);
        } else {
            return inflater.inflate(R.layout.fragment_requests_page, container, false);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Objects.equals(type, "requests")) {
            view.findViewById(R.id.chats_button).setOnClickListener(view1 -> {
                Fragment fragment = new MessagesPageFragment("chats");
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.pagesFragment, fragment);
                ft.commit();
            });
        } else {
            view.findViewById(R.id.requests_button).setOnClickListener(view1 -> {
                Fragment fragment = new MessagesPageFragment("requests");
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.pagesFragment, fragment);
                ft.commit();
            });
        }
    }
}