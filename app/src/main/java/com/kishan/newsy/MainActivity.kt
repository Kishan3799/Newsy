package com.kishan.newsy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kishan.newsy.adapter.NewsAdapter
import com.kishan.newsy.data.remote.NewsApi
import com.kishan.newsy.data.remote.RetrofitService
import com.kishan.newsy.data.repository.NewsRepository
import com.kishan.newsy.database.ArticleDatabase
import com.kishan.newsy.databinding.ActivityMainBinding
import com.kishan.newsy.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.http.Query


class MainActivity : AppCompatActivity(),View.OnClickListener{
    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter:NewsAdapter
    lateinit var mainViewModel: MainViewModel

    val MY_TAG = "TopHeadlinesNews"
    companion object {
        const val QUERY_PAGE_SIZE = 20
    }


    lateinit var button: Button
    var category: String = ""

    var isLoad = false
    var isLastPage = false
    var isScrolling = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(newsRepository)).get(MainViewModel::class.java)
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val intent = Intent(this, NewsContentActivity::class.java)
            intent.putExtra("article", it)
            startActivity(intent)
        }


        mainViewModel.topHeadlines.observe(this, Observer { response ->
            when(response){
                is Resource.Success -> {
                   hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPage = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage= mainViewModel.topHeadlinePage == totalPage
                        if(isLastPage) {
                            binding.newsRV.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                  hideProgressBar()
                    response.message?.let { message ->
                        Log.e(MY_TAG, "An Error occured : $message")
                    }
                }
                is Resource.Loading -> {
                   showProgressBar()
                }
            }
        })

        val button = listOf(
            binding.allBtn,
            binding.businessBtn,
            binding.healthBtn,
            binding.entertainmentBtn,
            binding.scienceBtn,
            binding.techBtn,
            binding.sportsBtn
        )

        button.forEach{btn->
            btn.setOnClickListener(this)
        }

        //important for toolbar
        setSupportActionBar(binding.topAppBar)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save_article -> {
//                Toast.makeText(this,"clicked" ,Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,SavedNewsActivity::class.java))
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View?) {
        button = view as Button
        category = button.text.toString()
        binding.progressCircular.visibility = View.VISIBLE
        mainViewModel.getTopHeadLineNews(category)
        binding.progressCircular.visibility = View.GONE

    }

    val scrollListener = object : RecyclerView.OnScrollListener(){
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoad && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                mainViewModel.getTopHeadLineNews(category)
                isScrolling = false
            }
        }

    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        binding.newsRV.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            addOnScrollListener(this@MainActivity.scrollListener)
        }
    }

    private fun hideProgressBar(){
        binding.progressCircular.visibility = View.GONE
        isLoad = false
    }

    private fun showProgressBar(){
        binding.progressCircular.visibility = View.VISIBLE
        isLoad = true
    }

}