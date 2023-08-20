package com.kishan.newsy.data.repository


import com.kishan.newsy.data.remote.NewsApi
import com.kishan.newsy.data.remote.RetrofitService
import com.kishan.newsy.database.ArticleDatabase
import com.kishan.newsy.model.Article

class NewsRepository(
    private val db : ArticleDatabase
    ){

    //General News
    suspend fun getTopHeadLineNews(newsPage: Int) =
        RetrofitService.getInstance().create(NewsApi::class.java).getAllArticles(newsCategory = "general", page = newsPage)

    //Business News
    suspend fun getBusinessNews(newsPage: Int) =
        RetrofitService.getInstance().create(NewsApi::class.java).getAllArticles(newsCategory = "business", page = newsPage)

    //Entertainment News
    suspend fun getEntertainmentNews(newsPage: Int) =
        RetrofitService.getInstance().create(NewsApi::class.java).getAllArticles(newsCategory = "entertainment", page = newsPage)

    //Health News
    suspend fun getHealthNews(newsPage: Int) =
        RetrofitService.getInstance().create(NewsApi::class.java).getAllArticles(newsCategory = "health", page = newsPage)

    //Science News
    suspend fun getScienceNews(newsPage: Int) =
        RetrofitService.getInstance().create(NewsApi::class.java).getAllArticles(newsCategory = "science", page = newsPage)

    //Sports News
    suspend fun getSportsNews(newsPage: Int) =
        RetrofitService.getInstance().create(NewsApi::class.java).getAllArticles(newsCategory = "sports", page = newsPage)

    // Technology News
    suspend fun getTechnologyNews(newsPage: Int) =
        RetrofitService.getInstance().create(NewsApi::class.java).getAllArticles(newsCategory = "technology", page = newsPage)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun gettingSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteNews(article: Article) = db.getArticleDao().deleteArticle(article)
}