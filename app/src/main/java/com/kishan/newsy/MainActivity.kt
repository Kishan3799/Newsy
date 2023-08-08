package com.kishan.newsy

import android.os.Bundle
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
import com.kishan.newsy.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var newsAdapter:NewsAdapter
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val newsyApi = RetrofitService.getInstance().create(NewsApi::class.java)
        val repository = NewsRepository(newsyApi)

        newsAdapter = NewsAdapter()

        mainViewModel = ViewModelProvider(this, MainViewModelFactory(repository)).get(MainViewModel::class.java)

        mainViewModel.article.observe(this, Observer {
            binding.progressCircular.visibility =View.GONE
            it.articles?.let { newsResponse ->
                newsAdapter.differ.submitList(newsResponse)
            }
            binding.newsRV.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = newsAdapter
            }
        })

        newsAdapter.setOnItemClickListener {
            Toast.makeText(this,it.title, Toast.LENGTH_SHORT).show()
        }




    }

    override fun onClick(view: View?) {
        val button = view as Button
    }


}