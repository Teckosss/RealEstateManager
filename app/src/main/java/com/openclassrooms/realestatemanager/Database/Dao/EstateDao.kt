package com.openclassrooms.realestatemanager.Database.Dao

import android.arch.persistence.room.Dao
import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.openclassrooms.realestatemanager.Models.Estate


/**
 * Created by Adrien Deguffroy on 05/10/2018.
 */

@Dao
interface EstateDao {

    @Query("SELECT * FROM Estate")
    fun getItems(): LiveData<List<Estate>>

    @Insert
    fun insertItem(estate: Estate) : Long

    @Update
    fun updateItem(estate: Estate)
}