package com.kishan.newsy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter:NewsAdapter
    lateinit var mainViewModel: MainViewModel

    val MY_TAG = "TopHeadlinesNews"

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
                    binding.progressCircular.visibility = View.GONE
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    binding.progressCircular.visibility = View.GONE
                    response.message?.let { message ->
                        Log.e(MY_TAG, "An Error occured : $message")
                    }
                }
                is Resource.Loading -> {
                    binding.progressCircular.visibility =View.VISIBLE
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
            btn.setOnClickListener(this@MainActivity)
        }





//        val newsyApi = RetrofitService.getInstance().create(NewsApi::class.java)
//        val repository = NewsRepository(newsyApi)
//
//        newsAdapter = NewsAdapter()
//

//
//        mainViewModel.article.observe(this, Observer {
//            binding.progressCircular.visibility =View.GONE
//            it.articles?.let { newsResponse ->
//                newsAdapter.differ.submitList(newsResponse)
//            }
//            binding.newsRV.apply {
//                layoutManager = LinearLayoutManager(this@MainActivity)
//                adapter = newsAdapter
//            }
//        })
//




    }

    override fun onClick(view: View?) {
        val button = view as Button
        val category = button.text.toString()
        binding.progressCircular.visibility = View.VISIBLE
        mainViewModel.getTopHeadLineNews(category)
        binding.progressCircular.visibility = View.GONE

    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        binding.newsRV.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }


}