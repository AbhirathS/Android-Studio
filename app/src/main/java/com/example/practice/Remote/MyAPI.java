package com.example.practice.Remote;

import com.example.practice.Model.Logininfo;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;

public interface MyAPI {

    @POST("api/register")
    Observable<String> registerUser(@Body Logininfo user);

    @POST("api/login")
    Observable<String> loginUser(@Body Logininfo user);

    @PUT("api/register")
    Observable<String> putUser(@Body Logininfo user);

    @GET("api/register")
    Observable<List<String>> getLink();
}
