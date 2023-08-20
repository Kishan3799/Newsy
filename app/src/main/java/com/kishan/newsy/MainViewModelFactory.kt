package com.kishan.newsy

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kishan.newsy.data.repository.NewsRepository

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(val app: Application, private val repository: NewsRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(app, repository) as T
    }

}