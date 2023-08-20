package com.kishan.newsy.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitService {
    private const val BASE_URL = "https://newsapi.org/"

    fun getInstance():Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}