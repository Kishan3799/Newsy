package com.kishan.newsy.data.remote

import com.kishan.newsy.BuildConfig
import com.kishan.newsy.model.NewsArticlesDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsApi {
    @Headers("Authorization: ${BuildConfig.API_KEY}")
    @GET("/v2/top-headlines")
    suspend fun getAllArticles(
        @Query("country")
        countryCode:String = "in",
        @Query("category")
        newsCategory:String = "general"
    ): Response<NewsArticlesDto>
}