package com.openclassrooms.realestatemanager.Database.Dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.openclassrooms.realestatemanager.Models.Image

/**
 * Created by Adrien Deguffroy on 10/10/2018.
 */

@Dao
interface ImageDao {

    @Query("SELECT * FROM Image WHERE estateId = :index")
    fun getItems(index:Long): LiveData<List<Image>>

    @Insert
    fun insertItem(image: Image) : Long

    @Update
    fun updateItem(image: Image)

    @Query("DELETE FROM Image WHERE id = :index")
    fun deleteItem(index: Long)
}