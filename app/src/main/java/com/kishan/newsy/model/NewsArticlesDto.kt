package com.kishan.newsy.model

data class NewsArticlesDto(
    val articles: List<Article>? = emptyList(),
    val status : String?="",
    val totalResults: Int? =0
)