package com.kishan.newsy.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kishan.newsy.MainViewModel
import com.kishan.newsy.MainViewModelFactory
import com.kishan.newsy.R
import com.kishan.newsy.data.repository.NewsRepository
import com.kishan.newsy.database.ArticleDatabase
import com.kishan.newsy.databinding.ActivityMainBinding
import com.kishan.newsy.ui.fragments.BusinessNewsFragment
import com.kishan.newsy.ui.fragments.EntertainmentNewsFragment
import com.kishan.newsy.ui.fragments.GeneralNewsFragment
import com.kishan.newsy.ui.fragments.HealthNewsFragment
import com.kishan.newsy.ui.fragments.ScienceNewsFragment
import com.kishan.newsy.ui.fragments.SportsNewsFragment
import com.kishan.newsy.ui.fragments.TechnologyNewsFragment



class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    lateinit var mainViewModel: MainViewModel

    companion object {
        const val QUERY_PAGE_SIZE = 20
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(application, newsRepository))[MainViewModel::class.java]

        val buttonToFragmentMap = mapOf(
            binding.allBtn to GeneralNewsFragment(),
            binding.businessBtn to BusinessNewsFragment(),
            binding.healthBtn to HealthNewsFragment(),
            binding.entertainmentBtn to EntertainmentNewsFragment(),
            binding.scienceBtn to ScienceNewsFragment(),
            binding.techBtn to TechnologyNewsFragment(),
            binding.sportsBtn to SportsNewsFragment()
        )

        buttonToFragmentMap.forEach { (button, fragment) ->
            setButtonClickListener(button, fragment)
        }

        //important for toolbar
        setSupportActionBar(binding.topAppBar)

    }

    private fun setButtonClickListener(button:View, fragment: Fragment){
        button.setOnClickListener {
            replaceFrameWithFragment(fragment)
        }
    }

    private fun replaceFrameWithFragment(fragment:Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragsFrameLayout, fragment)
        fragmentTransaction.commit()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.save_article -> {
//                Toast.makeText(this,"clicked" ,Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, SavedNewsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



}