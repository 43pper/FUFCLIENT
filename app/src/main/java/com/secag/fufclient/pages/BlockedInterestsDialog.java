package com.secag.fufclient.pages;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.secag.fufclient.App;
import com.secag.fufclient.Interest;
import com.secag.fufclient.R;
import com.secag.fufclient.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlockedInterestsDialog extends AppCompatDialogFragment {

    ArrayList<Integer> bannedInterests;

    public BlockedInterestsDialog(ArrayList<Integer> bannedInterests) {
        this.bannedInterests = bannedInterests;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Збереження")
                .setMessage("Зберегти заблоковані інтереси?")
                .setPositiveButton("Зберегти", (dialog, id) -> {
                    App.getFufApi().getInterestsByUserId(1).enqueue(new Callback<ArrayList<Interest>>() {

                        @Override
                        public void onResponse(Call<ArrayList<Interest>> call, Response<ArrayList<Interest>> response) {
                            if (response.body() == null) {
                                return;
                            }
                            ArrayList<Integer> interests = new ArrayList<>();
                            for (Interest item : response.body()) {
                                interests.add(item.getId());
                            }
                            App.getFufApi().editPreferencesById(1, interests, bannedInterests).enqueue(new Callback<User>() {

                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {

                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Interest>> call, Throwable t) {

                        }
                    });
                    dialog.cancel();
                })
                .setNegativeButton("Скасувати", (dialog, id) -> {
                    // Закрываем окно
                    dialog.cancel();
                });
        return builder.create();
    }
}