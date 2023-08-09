package com.kishan.newsy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.bumptech.glide.Glide
import com.kishan.newsy.databinding.ActivityNewsContentBinding
import com.kishan.newsy.model.Article


class NewsContentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsContentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val article = intent.getSerializableExtra("article") as Article
        binding.webView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }

    }
}