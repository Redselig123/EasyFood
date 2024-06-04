package com.example.easyfood.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.databinding.ActivityMealBinding
import com.example.easyfood.db.MealDatabase
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.pojo.Meal
import com.example.easyfood.viewModel.MealViewModel
import com.example.easyfood.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var mealMvvm : MealViewModel
    private lateinit var youtubeLink : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this, viewModelFactory).get(MealViewModel::class.java)

        getMealInformation()
        setInformationInView()

        onLoadingCase()
        mealMvvm.getMealDetail(mealId)
        observeMealDetail()

        binding.imgYoutube.setOnClickListener{
            onYoutubeImgClick()
        }
        onFavoriteClick()

    }

    private fun onFavoriteClick() {
        binding.btnFavoriteButton.setOnClickListener {
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this, "Meal saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeImgClick() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
        startActivity(intent)
    }

    private var mealToSave: Meal?=null
    private fun observeMealDetail() {
        mealMvvm.observeMealDetailLiveData().observe(this)
        { meal->
            onResponseCase()
            binding.tvMealCategory.text = "Category: ${meal.strCategory}"
            binding.tvMealArea.text = "Area: ${meal.strArea}"
            binding.tvMealInstructions.text = meal.strInstructions

            mealToSave = meal

            youtubeLink = meal.strYoutube.toString()
        }
    }

    private fun setInformationInView() {
        Glide.with(this)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolBar.title = mealName
        binding.collapsingToolBar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolBar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getMealInformation() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    fun onLoadingCase(){
        binding.progressBar.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.INVISIBLE
        binding.tvMealArea.visibility = View.INVISIBLE
        binding.tvMealCategory.visibility = View.INVISIBLE
        binding.btnFavoriteButton.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE
    }
    fun onResponseCase(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvMealArea.visibility = View.VISIBLE
        binding.tvMealCategory.visibility = View.VISIBLE
        binding.btnFavoriteButton.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
    }

}