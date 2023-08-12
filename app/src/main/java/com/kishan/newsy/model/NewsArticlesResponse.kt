package com.kishan.newsy.model

import java.io.Serializable

data class NewsArticlesResponse(
    val articles: MutableList<Article>,
    val status : String,
    val totalResults: Int
):Serializable