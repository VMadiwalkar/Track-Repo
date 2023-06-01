package com.example.myapplication

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

public interface ApiInterface  {

    @GET("/repos/{owner}/{repo}")
    fun  getRepo(@Path("owner")owner:String,@Path("repo")repo:String,): Call<RepoModel>
}