package com.example.easyfood.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.easyfood.R
import com.example.easyfood.activities.CategoryMealsActivity
import com.example.easyfood.activities.MainActivity
import com.example.easyfood.activities.MealActivity
import com.example.easyfood.adapter.CategoriesAdapter
import com.example.easyfood.adapter.CategoriesMealsAdapter
import com.example.easyfood.adapter.MostPopularAdapter
import com.example.easyfood.databinding.FragmentHomeBinding
import com.example.easyfood.fragments.bottomSheet.MealBottomSheetFragment
import com.example.easyfood.pojo.MealByCategory
import com.example.easyfood.pojo.Meal
import com.example.easyfood.viewModel.HomeViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal: Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var categoryMealsAdapter: CategoriesMealsAdapter

    companion object {
        const val MEAL_ID = "com.example.easyfood.fragments.idMeal"
        const val MEAL_NAME = "com.example.easyfood.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.easyfood.fragments.thumbMeal"
        const val CATEGORY_NAME = "com.example.easyfood.fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        popularItemsAdapter = MostPopularAdapter()
        categoryMealsAdapter = CategoriesMealsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemsRecyclerView()


        viewModel.getRandomMeal()
        observerRandomMeal()

        onRandomMealClick()

        viewModel.getPopularItems()
        observerPopularItemsLiveData()
        onPopularItemClick()

        prepareCategoriesItemRecyclerView()
        viewModel.getCategories()
        observerCategoriesLiveData()

        onCategoryClick()

        onPopularLongItemClick()

        onSearchIconClick()
    }

    private fun onSearchIconClick() {
        binding.imgSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onPopularLongItemClick() {
        popularItemsAdapter.onLongItemClick = { meal ->
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager, "Meal Info")
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = { category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesItemRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        binding.recyclreViewCategory.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun observerCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner) { categories ->
            categoriesAdapter.setCategoriesList(categories)
        }
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recycleViewPopularItems.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun observerPopularItemsLiveData() {
        viewModel.observePopularItemsLiveData().observe(
            viewLifecycleOwner
        ) { mealList ->
            popularItemsAdapter.setMeals(mealList = mealList as ArrayList<MealByCategory>)


        }

    }

    private fun onRandomMealClick() {
        binding.imgCardView.setOnClickListener {
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }

    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLiveData()
            .observe(
                viewLifecycleOwner
            ) { meal ->
                Glide.with(this@HomeFragment)
                    .load(meal.strMealThumb)
                    .into(binding.imgCardView)

                this.randomMeal = meal
            }
    }

}