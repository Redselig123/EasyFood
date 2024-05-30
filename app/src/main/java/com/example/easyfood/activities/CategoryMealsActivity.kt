package com.example.easyfood.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.easyfood.adapter.CategoriesMealsAdapter
import com.example.easyfood.databinding.ActivityCategoryMealsBinding
import com.example.easyfood.fragments.HomeFragment
import com.example.easyfood.viewModel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {
    lateinit var binding : ActivityCategoryMealsBinding
    lateinit var categoryMealViewModel : CategoryMealsViewModel
    lateinit var categoryMealAdapter:CategoriesMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()

        categoryMealViewModel = ViewModelProviders.of(this)[CategoryMealsViewModel::class.java]

        categoryMealViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)
        categoryMealViewModel.observeMealsLiveData().observe(this, Observer {mealsList ->
           /* mealsList.forEach{
                Log.d("test", it.strMeal)
            } check the call was success*/
            binding.tvCategoryCount.text = mealsList.size.toString()
            categoryMealAdapter.setMealsList(mealsList)

        })
    }

    private fun prepareRecyclerView() {
        categoryMealAdapter = CategoriesMealsAdapter()
        binding.rvMeals.apply{
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            adapter = categoryMealAdapter
        }
    }
}