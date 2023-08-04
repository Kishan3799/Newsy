package com.kishan.newsy.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kishan.newsy.databinding.NewsItemCardBinding
import com.kishan.newsy.model.NewsArticlesDto

class NewsAdapter(private val context:Context, private val newsArticles:NewsArticlesDto):RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(val binding: NewsItemCardBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return newsArticles.articles.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val data = newsArticles.articles[position]
        holder.apply {
            val imageView = binding.newsImage
            Glide.with(context).load(data.urlToImage).into(imageView)
            binding.newsTitle.text = data.title
            binding.newsDesc.text = data.description
            binding.newsPublished.text = data.publishedAt
        }
    }
}