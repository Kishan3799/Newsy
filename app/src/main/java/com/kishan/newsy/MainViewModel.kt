package com.kishan.newsy


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.kishan.newsy.data.repository.NewsRepository
import com.kishan.newsy.model.Article
import com.kishan.newsy.model.NewsArticlesResponse
import com.kishan.newsy.utils.Resource
import kotlinx.coroutines.launch

import retrofit2.Response

class MainViewModel(
    private val repository: NewsRepository
    ) : ViewModel() {

    val topHeadlines : MutableLiveData<Resource<NewsArticlesResponse>> = MutableLiveData()
    var topHeadlinePage = 1
    var topHeadlinesResponse: NewsArticlesResponse? = null

    init {
        getTopHeadLineNews("general")
    }

    fun getTopHeadLineNews(category: String) = viewModelScope.launch {
        topHeadlines.postValue(Resource.Loading())
        val response = repository.getTopHeadLineNews(category,topHeadlinePage)
        topHeadlines.postValue(handlingTopHeadlineResponse(response))
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





}