package com.kishan.newsy.data.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kishan.newsy.data.remote.NewsApi
import com.kishan.newsy.data.remote.RetrofitService
import com.kishan.newsy.database.ArticleDatabase
import com.kishan.newsy.model.NewsArticlesDto

class NewsRepository(
//    private val newsResponse: NewsApi
    val db : ArticleDatabase
    ){

//    private val articleLiveData = MutableLiveData<NewsArticlesDto>()
//
//    val articles:LiveData<NewsArticlesDto>
//        get() = articleLiveData
//
//     suspend fun getTopHeadLineNews(category: String, page:Int){
//        val result = newsResponse.getAllArticles(newsCategory = category, page = page)
//         if(result.body() != null){
//             articleLiveData.postValue(result.body())
//         }
//     }

    suspend fun getTopHeadLineNews(newsCategory:String, newsPage: Int) =
        RetrofitService.getInstance().create(NewsApi::class.java).getAllArticles(newsCategory = newsCategory, page = newsPage)
}