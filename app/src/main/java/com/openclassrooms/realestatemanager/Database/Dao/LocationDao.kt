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

    @Query("SELECT * FROM Location")
    fun getItems(): LiveData<List<Location>>

    @Insert
    fun insertItem(location: Location) : Long

    @Update
    fun updateItem(location: Location)
}