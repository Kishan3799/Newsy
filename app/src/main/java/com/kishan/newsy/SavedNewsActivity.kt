package com.kishan.newsy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.kishan.newsy.adapter.NewsAdapter
import com.kishan.newsy.data.repository.NewsRepository
import com.kishan.newsy.database.ArticleDatabase
import com.kishan.newsy.databinding.ActivitySavedNewsBinding

class SavedNewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySavedNewsBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var newsAdapter: NewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val newsRepository = NewsRepository(ArticleDatabase(this))
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(application,newsRepository)).get(MainViewModel::class.java)
        binding = ActivitySavedNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()

        setSupportActionBar(binding.savedToolbar)
        binding.savedToolbar.setNavigationOnClickListener {
            finish()
        }

        newsAdapter.setOnItemClickListener {
            val intent = Intent(this, NewsContentActivity::class.java)
            intent.putExtra("article", it)
            startActivity(intent)
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                mainViewModel.delete(article)
                Snackbar.make(this@SavedNewsActivity.findViewById(R.id.savedNews), "Article is Deleted", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo") {
                        mainViewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.savedNewsRv)
        }

        mainViewModel.getAllArticles().observe(this, Observer {articles->
            newsAdapter.differ.submitList(articles)
        })


    }
    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        binding.savedNewsRv.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(this@SavedNewsActivity)
        }
    }
}