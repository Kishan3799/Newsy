package com.kishan.newsy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.load.engine.Resource
import com.kishan.newsy.data.repository.NewsRepository
import com.kishan.newsy.model.NewsArticlesDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: NewsRepository):ViewModel() {



    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getTopHeadLineNews(category = "general", page = 1)
        }
    }

    val article : LiveData<NewsArticlesDto>
        get() = repository.articles




}