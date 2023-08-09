package com.kishan.newsy.model

import java.io.Serializable

data class NewsArticlesDto(
    val articles: List<Article>? = emptyList(),
    val status : String?="",
    val totalResults: Int? =0
):Serializable