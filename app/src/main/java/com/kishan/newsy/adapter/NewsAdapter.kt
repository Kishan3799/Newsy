package com.kishan.newsy.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kishan.newsy.databinding.NewsItemCardBinding
import com.kishan.newsy.model.Article
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class NewsAdapter :RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(val binding: NewsItemCardBinding):RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = differ.currentList[position]

        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(holder.binding.newsImage)
            holder.binding.newsTitle.text = article!!.title
            holder.binding.newsDesc.text = article.description
            dateFormatter(article.publishedAt, holder.binding.newsPublished)
            setOnClickListener {
                onItemClickListener?.let { it(article) }
                Log.d("click", article.title)
            }
        }

    }

    private var onItemClickListener : ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener: (Article)-> Unit){
        onItemClickListener = listener
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun dateFormatter(articleDate:String, textView: TextView){
    val timestamp = Instant.parse(articleDate)
    val articlePublishedZonedTime = ZonedDateTime.ofInstant(timestamp, ZoneId.systemDefault())
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
    textView.text = articlePublishedZonedTime.format(dateFormatter)
}

