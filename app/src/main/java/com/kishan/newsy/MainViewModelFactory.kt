package com.kishan.newsy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kishan.newsy.data.repository.NewsRepository

class MainViewModelFactory(private val repository: NewsRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }

}