package com.kishan.newsy.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kishan.newsy.utils.Resource
import com.kishan.newsy.MainViewModel
import com.kishan.newsy.MainViewModelFactory
import com.kishan.newsy.R
import com.kishan.newsy.adapter.NewsAdapter
import com.kishan.newsy.data.repository.NewsRepository
import com.kishan.newsy.database.ArticleDatabase
import com.kishan.newsy.databinding.ActivitySearchNewsBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsActivity : AppCompatActivity() {
    lateinit var binding : ActivitySearchNewsBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var newsAdapter: NewsAdapter

    val SEARCH_NEWS_TIME_DELAY = 500L

    var isError = false
    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        mainViewModel =  ViewModelProvider(this, MainViewModelFactory(application,newsRepository))[MainViewModel::class.java]
        binding = ActivitySearchNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()

        setSupportActionBar(binding.searchToolbar)
        binding.searchToolbar.setNavigationOnClickListener {
            finish()
        }

        newsAdapter.setOnItemClickListener {
            val intent = Intent(this, NewsContentActivity::class.java)
            intent.putExtra("article", it)
            startActivity(intent)
        }

        var job :Job? = null
        binding.searchBar.addTextChangedListener {editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                    editable?.let {
                        if(editable.toString().isNotEmpty()){
                            mainViewModel.searchNews(editable.toString())
                        }
                    }
            }
        }

        mainViewModel.searchNews.observe(this, Observer {response ->
            when(response){
                is Resource.Success ->{
                    hideProgressBar()
                    response.data?.let {
                        newsAdapter.differ.submitList(it.articles.toList())
                        val totalPage = it.totalResults / MainActivity.QUERY_PAGE_SIZE + 2
                        isLastPage = mainViewModel.searchNewsPage == totalPage
                        if(isLastPage){
                            binding.searchNewsRv.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(this, "An error occured: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })


    }

    private fun hideProgressBar() {
        binding.progressCircularBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.progressCircularBar.visibility = View.VISIBLE
        isLoading = true
    }

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= MainActivity.QUERY_PAGE_SIZE
            val shouldPaginate = isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                mainViewModel.searchNews(binding.searchBar.text.toString())
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        binding.searchNewsRv.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@SearchNewsActivity)
        }
    }
}