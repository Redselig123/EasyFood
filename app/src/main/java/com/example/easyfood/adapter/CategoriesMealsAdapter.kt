package com.example.easyfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.easyfood.databinding.MealItemBinding
import com.example.easyfood.pojo.MealByCategory

class CategoriesMealsAdapter : RecyclerView.Adapter<CategoriesMealsAdapter.CategoriesMealsViewModel>() {
    private var mealList = ArrayList<MealByCategory>()

    fun setMealsList(mealList:List<MealByCategory>){
        this.mealList = mealList as ArrayList<MealByCategory>
        notifyDataSetChanged()
    }

    inner class CategoriesMealsViewModel(val binding:MealItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesMealsViewModel {
        return CategoriesMealsViewModel(MealItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    override fun onBindViewHolder(holder: CategoriesMealsViewModel, position: Int) {
        Glide.with(holder.itemView).load(mealList[position].strMealThumb).into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = mealList[position].strMeal
    }
}