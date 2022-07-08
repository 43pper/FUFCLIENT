package com.secag.fufclient;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FufApi {
    @GET("/interests/all")
    Call<ArrayList<Interest>> getAllInterests();
    @GET("/profiles/{id}/interests")
    Call<ArrayList<Interest>> getInterestsByUserId(@Path("id") int id);
    @GET("/profiles/{id}/bannedinterests")
    Call<ArrayList<Interest>> getBlockedInterestsByUserId(@Path("id") int id);
    @GET("/profiles")
    Call<ArrayList<User>> getProfiles();
    @GET("/profiles/{id}")
    Call<User> getProfileById(@Path("id") int id);
    @GET("/interests/{id}")
    Call<Interest> getInterestById(@Path("id") int id);
    @POST("/feed/{id}")
    Call<ArrayList<User>> getFeedRecommendations(@Path("id") int id, @Query("profilesNumber") int count);
    @PUT("/profiles/{id}")
    Call<User> editPreferencesById(@Path("id") int id, @Query("interests") ArrayList<Integer> interests, @Query("bannedInterests") ArrayList<Integer> bannedInterests);
}
