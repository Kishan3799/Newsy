package com.kishan.newsy


import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_MOBILE
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.kishan.newsy.data.repository.NewsRepository
import com.kishan.newsy.model.Article
import com.kishan.newsy.model.NewsArticlesResponse
import com.kishan.newsy.utils.Resource
import kotlinx.coroutines.launch

import retrofit2.Response
import java.io.IOException

class MainViewModel(
    app:Application,
    private val repository: NewsRepository
    ) : AndroidViewModel(app) {

    val topHeadlines : MutableLiveData<Resource<NewsArticlesResponse>> = MutableLiveData()
    var topHeadlinePage = 1
    var topHeadlinesResponse: NewsArticlesResponse? = null

    init {
        getTopHeadLineNews("general")
    }

    fun getTopHeadLineNews(category: String) = viewModelScope.launch {
       safeTopHeadlineCalls(category)
    }

    fun handlingTopHeadlineResponse(response: Response<NewsArticlesResponse>) : Resource<NewsArticlesResponse> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                topHeadlinePage++
                if(topHeadlinesResponse == null){
                    topHeadlinesResponse = resultResponse
                } else {
                    val oldArticle = topHeadlinesResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(topHeadlinesResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.upsert(article)
    }

    fun getAllArticles() = repository.gettingSavedNews()

    fun delete(article: Article) = viewModelScope.launch {
        repository.deleteNews(article)
    }

    private suspend fun safeTopHeadlineCalls(category: String){
        topHeadlines.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = repository.getTopHeadLineNews(category, topHeadlinePage)
                topHeadlines.postValue(handlingTopHeadlineResponse(response))
            } else {
                topHeadlines.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable){
            when(t){
                is IOException -> topHeadlines.postValue(Resource.Error("Network Failure"))
                else -> topHeadlines.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsyApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }






}