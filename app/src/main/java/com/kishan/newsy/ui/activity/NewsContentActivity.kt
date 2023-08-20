package com.kishan.newsy.ui.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.kishan.newsy.MainViewModel
import com.kishan.newsy.MainViewModelFactory
import com.kishan.newsy.R
import com.kishan.newsy.data.repository.NewsRepository
import com.kishan.newsy.database.ArticleDatabase
import com.kishan.newsy.databinding.ActivityNewsContentBinding
import com.kishan.newsy.model.Article


class NewsContentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsContentBinding
    lateinit var mainViewModel: MainViewModel
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val newsRepository = NewsRepository(ArticleDatabase(this))
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(application,newsRepository))[MainViewModel::class.java]
        binding = ActivityNewsContentBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val article = intent.getSerializableExtra("article") as Article
        binding.webView.apply {
            settings.javaScriptEnabled = true
            loadUrl(article.url)
            webViewClient = WebViewClient()
        }

        binding.saveBtn.setOnClickListener {
            mainViewModel.saveArticle(article)
            Snackbar.make(this.findViewById(R.id.newsContent),"Article saved successfully", Snackbar.LENGTH_SHORT).show()
        }

        binding.backScreenButton.setOnClickListener {
            finish()
        }

    }
}