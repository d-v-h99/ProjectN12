package com.example.projectn12.api;

import com.example.projectn12.models.Districts;
import com.example.projectn12.models.Provinces;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface JsonPlaceHolderAPI {
    @GET("p")
    Call<List<Provinces>> getProvinces();

    @GET("p/{id}")
    Call<Provinces> getDistricts(@Path("id") int id, @Query("depth") int depth);
    @GET("d/{id}")
    Call<Districts> getWards(@Path("id") int id, @Query("depth") int depth);
}