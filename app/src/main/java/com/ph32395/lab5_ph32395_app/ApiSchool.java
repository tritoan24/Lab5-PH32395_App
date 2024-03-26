package com.ph32395.lab5_ph32395_app;

import retrofit2.Call;
        import retrofit2.http.Body;
        import retrofit2.http.DELETE;
        import retrofit2.http.GET;
        import retrofit2.http.POST;
        import retrofit2.http.PUT;
        import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface ApiSchool {
    String BASE_URL = "http://192.168.0.103:3001/";

    @GET("api/")
    Call<List<School>> getSchool();

    @POST("api/them")
    Call<School> themSchool(@Body School sc);

    @PUT("api/sua/{id}")
    Call<School> suaSchool(@Path("id") String id, @Body School sc);

    @DELETE("api/xoa/{id}")
    Call<Void> xoaSchool(@Path("id") String id);
    @GET("api/timkiem")
    Call<List<School>> searchSchool(@Query("name") String keyword);
}