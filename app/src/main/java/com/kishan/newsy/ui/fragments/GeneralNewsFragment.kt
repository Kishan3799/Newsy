package com.kishan.newsy.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kishan.newsy.ui.activity.MainActivity
import com.kishan.newsy.ui.activity.MainActivity.Companion.QUERY_PAGE_SIZE
import com.kishan.newsy.MainViewModel
import com.kishan.newsy.ui.activity.NewsContentActivity
import com.kishan.newsy.R
import com.kishan.newsy.adapter.NewsAdapter
import com.kishan.newsy.utils.Resource

class GeneralNewsFragment : Fragment(R.layout.general_news_fragment) {
    lateinit var mainViewModel: MainViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var generalNewsRecyclerView: RecyclerView
    lateinit var progressCircularBar: ProgressBar

    var isLoad = false
    var isLastPage = false
    var isScrolling = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressCircularBar = view.findViewById(R.id.progress_circular)
        generalNewsRecyclerView = view.findViewById(R.id.generalRv)

        mainViewModel = (activity as MainActivity).mainViewModel
        mainViewModel.getTopHeadLineNews()
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val intent = Intent(requireContext(), NewsContentActivity::class.java)
            intent.putExtra("article", it)
            startActivity(intent)
        }

        mainViewModel.topHeadlines.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPage = newsResponse.totalResults / MainActivity.QUERY_PAGE_SIZE + 2
                        isLastPage = mainViewModel.topHeadlinePage == totalPage
                        if (isLastPage) {
                            generalNewsRecyclerView.setPadding(0, 0, 0, 0)
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(
                            requireContext(),
                            "Please Check Your Internet Connection ",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }


        
    }

    private val scrollListener = object : RecyclerView.OnScrollListener(){
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
                mainViewModel.getTopHeadLineNews()
                isScrolling = false
            }
        }
    }

    private fun setupRecyclerView(){
        newsAdapter = NewsAdapter()
        generalNewsRecyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addOnScrollListener(this@GeneralNewsFragment.scrollListener)
        }
    }

    private fun hideProgressBar(){
        progressCircularBar.visibility = View.GONE
        isLoad = false
    }

    private fun showProgressBar(){
        progressCircularBar.visibility = View.VISIBLE
        isLoad = true
    }


}