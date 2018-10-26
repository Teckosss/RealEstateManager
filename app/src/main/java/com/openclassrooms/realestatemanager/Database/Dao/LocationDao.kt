package com.openclassrooms.realestatemanager.Database.Dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.openclassrooms.realestatemanager.Models.Location

/**
 * Created by Adrien Deguffroy on 16/10/2018.
 */

@Dao
interface LocationDao {

    @Query("SELECT * FROM Location WHERE estateId = :index")
    fun getItems(index:Long): LiveData<List<Location>>

    @Query("SELECT * FROM Location WHERE estateId = :index")
    fun getItemId(index:Long): Long

    @Insert
    fun insertItem(location: Location) : Long

    @Update
    fun updateItem(location: Location)
}