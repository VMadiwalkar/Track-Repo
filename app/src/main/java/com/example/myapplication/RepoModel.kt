package com.example.myapplication

import com.google.gson.annotations.SerializedName

data class RepoModel (
    val name:String,
    val description:String,
    @SerializedName("html_url")
    val url:String

)