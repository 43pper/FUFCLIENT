package com.secag.fufclient;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class User {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("profileDescription")
    @Expose
    private String profileDescription;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Location> favouriteLocations;

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setProfileDescription(String profileDescription) {
        this.profileDescription = profileDescription;
    }

    public String getProfileDescription() {
        return profileDescription;
    }

    public void setFavouriteLocations(List<Location> favouriteLocations) {
        this.favouriteLocations = favouriteLocations;
    }

    public List<Location> getFavouriteLocations() {
        return favouriteLocations;
    }

    public ArrayList<com.secag.fufclient.Interest> prefs = new ArrayList<>();
}
