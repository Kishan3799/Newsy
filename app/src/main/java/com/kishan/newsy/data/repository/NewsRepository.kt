package com.kishan.newsy.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kishan.newsy.data.remote.NewsApi
import com.kishan.newsy.model.NewsArticlesDto

class NewsRepository(private val newsResponse: NewsApi) {

    private val articleLiveData = MutableLiveData<NewsArticlesDto>()

    val articles:LiveData<NewsArticlesDto>
        get() = articleLiveData

     suspend fun getTopHeadLineNews(category: String, page:Int){
        val result = newsResponse.getAllArticles(newsCategory = category, page = page)
         if(result.body() != null){
             articleLiveData.postValue(result.body())
         }
     }
}