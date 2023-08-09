package com.kishan.newsy


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.kishan.newsy.data.repository.NewsRepository
import com.kishan.newsy.model.NewsArticlesDto
import com.kishan.newsy.utils.Resource
import kotlinx.coroutines.launch

import retrofit2.Response

class MainViewModel(
    private val repository: NewsRepository
    ) : ViewModel() {

    val topHeadlines : MutableLiveData<Resource<NewsArticlesDto>> = MutableLiveData()
    val topHeadlinePage = 1

    init {
        getTopHeadLineNews("general")
    }

    fun getTopHeadLineNews(category: String) = viewModelScope.launch {
        topHeadlines.postValue(Resource.Loading())
        val response = repository.getTopHeadLineNews(category,topHeadlinePage)
        topHeadlines.postValue(handlingTopHeadlineResponse(response))
    }

    fun handlingTopHeadlineResponse(response: Response<NewsArticlesDto>) : Resource<NewsArticlesDto> {
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

//    init {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.getTopHeadLineNews(category = "general", page = 1)
//        }
//    }
//
//    val article : LiveData<NewsArticlesDto>
//        get() = repository.articles




}