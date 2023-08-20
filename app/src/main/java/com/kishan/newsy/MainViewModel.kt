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

    //general News
    val topHeadlines : MutableLiveData<Resource<NewsArticlesResponse>> = MutableLiveData()
    var topHeadlinePage = 1
    private var topHeadlinesResponse: NewsArticlesResponse? = null

    //business News
    val businessNews : MutableLiveData<Resource<NewsArticlesResponse>> = MutableLiveData()
    var businessNewsPage = 1
    private var businessNewsResponse : NewsArticlesResponse? = null

    //entertainment News
    val entertainmentNews : MutableLiveData<Resource<NewsArticlesResponse>> = MutableLiveData()
    var entertainmentNewsPage = 1
    private var entertainmentNewsResponse : NewsArticlesResponse? = null

    //health News
    val healthNews : MutableLiveData<Resource<NewsArticlesResponse>> = MutableLiveData()
    var healthNewsPage = 1
    private var healthNewsResponse : NewsArticlesResponse? = null

    //Science News
    val scienceNews : MutableLiveData<Resource<NewsArticlesResponse>> = MutableLiveData()
    var scienceNewsPage = 1
    private var scienceNewsResponse : NewsArticlesResponse? = null

    //sports News
    val sportsNews : MutableLiveData<Resource<NewsArticlesResponse>> = MutableLiveData()
    var sportsNewsPage = 1
    private var sportsNewsResponse : NewsArticlesResponse? = null

    //technology News
    val technologyNews : MutableLiveData<Resource<NewsArticlesResponse>> = MutableLiveData()
    var technologyNewsPage = 1
    private var technologyNewsResponse : NewsArticlesResponse? = null


    init {
        getTopHeadLineNews()
    }

    //generalNewsResponse
    fun getTopHeadLineNews() = viewModelScope.launch {
       safeTopHeadlineCalls()
    }

    //business News Response
    fun getBusinessNews() = viewModelScope.launch {
        safeBusinessNewsCalls()
    }

    //entertainment News Response
    fun getEntertainmentNews() = viewModelScope.launch {
        safeEntertainmentCalls()
    }

    //Health News Response
    fun getHealthNews() = viewModelScope.launch {
        safeHealthNewsCalls()
    }

    //science news response
    fun getScienceNews() = viewModelScope.launch {
        safeScienceNewsCalls()
    }

    //sports news response
    fun getSportsNews() = viewModelScope.launch {
        safeSportsNewsCalls()
    }

    //technology news response
    fun getTechnologyNews() = viewModelScope.launch {
        safeTechnologyNewsCalls()
    }


    private fun handlingTopHeadlineResponse(response: Response<NewsArticlesResponse>) : Resource<NewsArticlesResponse> {
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

    //handling business news response
    private fun handlingBusinessNewsResponse(response: Response<NewsArticlesResponse>) : Resource<NewsArticlesResponse> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                businessNewsPage++
                if(businessNewsResponse == null){
                    businessNewsResponse = resultResponse
                } else {
                    val oldArticle = businessNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(businessNewsResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    //handling entertainment news response
    private fun handlingEntertainmentNewsResponse(response: Response<NewsArticlesResponse>) : Resource<NewsArticlesResponse> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                entertainmentNewsPage++
                if(entertainmentNewsResponse == null){
                    entertainmentNewsResponse = resultResponse
                } else {
                    val oldArticle = entertainmentNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(entertainmentNewsResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    //handling health news response
    private fun handlingHealthNewsResponse(response: Response<NewsArticlesResponse>) : Resource<NewsArticlesResponse> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                healthNewsPage++
                if(healthNewsResponse == null){
                    healthNewsResponse = resultResponse
                } else {
                    val oldArticle = healthNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(healthNewsResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    //handling science news response
    private fun handlingScienceNewsResponse(response: Response<NewsArticlesResponse>) : Resource<NewsArticlesResponse> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                scienceNewsPage++
                if(scienceNewsResponse == null){
                    scienceNewsResponse = resultResponse
                } else {
                    val oldArticle = scienceNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(scienceNewsResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    //handling sports news response
    private fun handlingSportsNewsResponse(response: Response<NewsArticlesResponse>) : Resource<NewsArticlesResponse> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                sportsNewsPage++
                if(sportsNewsResponse == null){
                    sportsNewsResponse = resultResponse
                } else {
                    val oldArticle = sportsNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(sportsNewsResponse ?: resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    //handling technology news response
    private fun handlingTechnologyNewsResponse(response: Response<NewsArticlesResponse>) : Resource<NewsArticlesResponse> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                technologyNewsPage++
                if(technologyNewsResponse == null){
                    technologyNewsResponse = resultResponse
                } else {
                    val oldArticle = technologyNewsResponse?.articles
                    val newArticle = resultResponse.articles
                    oldArticle?.addAll(newArticle)
                }
                return Resource.Success(technologyNewsResponse ?: resultResponse)
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

    //general news calls
    private suspend fun safeTopHeadlineCalls() {
        topHeadlines.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = repository.getTopHeadLineNews(topHeadlinePage)
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

    //business news calls
    private suspend fun safeBusinessNewsCalls() {
        businessNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = repository.getBusinessNews(businessNewsPage)
                businessNews.postValue(handlingBusinessNewsResponse(response))
            }else {
                businessNews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable){
            when(t){
                is IOException -> businessNews.postValue(Resource.Error("Network Failure"))
                else -> businessNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    //entertainment news calls
    private suspend fun safeEntertainmentCalls() {
        entertainmentNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = repository.getEntertainmentNews(entertainmentNewsPage)
                entertainmentNews.postValue(handlingEntertainmentNewsResponse(response))
            }else {
                entertainmentNews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable){
            when(t){
                is IOException -> entertainmentNews.postValue(Resource.Error("Network Failure"))
                else -> entertainmentNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    //health news calls
    private suspend fun safeHealthNewsCalls() {
        healthNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = repository.getHealthNews(healthNewsPage)
                healthNews.postValue(handlingHealthNewsResponse(response))
            }else {
                healthNews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable){
            when(t){
                is IOException -> healthNews.postValue(Resource.Error("Network Failure"))
                else -> healthNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    //science news calls
    private suspend fun safeScienceNewsCalls() {
        scienceNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = repository.getScienceNews(scienceNewsPage)
                scienceNews.postValue(handlingScienceNewsResponse(response))
            }else {
                scienceNews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable){
            when(t){
                is IOException -> scienceNews.postValue(Resource.Error("Network Failure"))
                else -> scienceNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    //sports news calls
    private suspend fun safeSportsNewsCalls() {
        sportsNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = repository.getSportsNews(sportsNewsPage)
                sportsNews.postValue(handlingSportsNewsResponse(response))
            }else {
                sportsNews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable){
            when(t){
                is IOException -> sportsNews.postValue(Resource.Error("Network Failure"))
                else -> sportsNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    //technology news calls
    private suspend fun safeTechnologyNewsCalls() {
        technologyNews.postValue(Resource.Loading())
        try {
            if(hasInternetConnection()){
                val response = repository.getTechnologyNews(technologyNewsPage)
                technologyNews.postValue(handlingTechnologyNewsResponse(response))
            }else {
                technologyNews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable){
            when(t){
                is IOException -> technologyNews.postValue(Resource.Error("Network Failure"))
                else -> technologyNews.postValue(Resource.Error("Conversion Error"))
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