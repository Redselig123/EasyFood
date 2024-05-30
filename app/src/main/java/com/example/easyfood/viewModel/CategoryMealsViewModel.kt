package com.example.easyfood.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easyfood.pojo.MealByCategory
import com.example.easyfood.pojo.MealsByCategoryList
import com.example.easyfood.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//its different activity, need this new viewmodel
class CategoryMealsViewModel : ViewModel() {
    val mealsLiveData = MutableLiveData<List<MealByCategory>>()

    fun getMealsByCategory(categoryName:String){
        RetrofitInstance.api.getMealsByCategory(categoryName).enqueue(object : Callback<MealsByCategoryList>{
            override fun onResponse(
                call: Call<MealsByCategoryList>,
                response: Response<MealsByCategoryList>
            ) {
                response.body()?.let {mealList ->
                    mealsLiveData.postValue(mealList.meals)

                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.e("CategoryMealsVireModel", t.message.toString())
            }

        })
    }

    fun observeMealsLiveData():LiveData<List<MealByCategory>>{
        return mealsLiveData
    }
}