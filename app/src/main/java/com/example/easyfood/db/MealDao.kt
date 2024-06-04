package com.example.easyfood.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.easyfood.pojo.Meal
import androidx.room.Query

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)//if exists update, if not, insert
    suspend fun upSert(meal:Meal)

    @Delete
    suspend fun delete(meal:Meal)

    @Query("SELECT * FROM mealInformation")
    fun getAllMeals():LiveData<List<Meal>>
}