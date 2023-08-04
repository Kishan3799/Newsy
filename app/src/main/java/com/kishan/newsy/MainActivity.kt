package com.kishan.newsy

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.kishan.newsy.adapter.NewsAdapter
import com.kishan.newsy.data.remote.NewsApi
import com.kishan.newsy.data.remote.RetrofitService
import com.kishan.newsy.databinding.ActivityMainBinding
import com.kishan.newsy.model.NewsArticlesDto
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.create

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter:NewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val newsyApi = RetrofitService.getInstance().create(NewsApi::class.java)



        GlobalScope.launch {

            try {
                val result = newsyApi.getAllArticles("in", "general")

                if (result.isSuccessful){
                    runOnUiThread(Runnable {
                        binding.newsRV.apply {
                            layoutManager = LinearLayoutManager(this@MainActivity)
                            adapter = NewsAdapter(this@MainActivity, NewsArticlesDto(articles = result.body()!!.articles))
                            binding.progressCircular.visibility = View.GONE
                        }
                    })

                }else{
                    Toast.makeText(
                        this@MainActivity,
                        result.errorBody().toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }catch (e:Exception){
                e.localizedMessage?.let { Log.d("Error", it) }
            }
        }








    }
}