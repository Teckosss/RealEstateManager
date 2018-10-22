package com.openclassrooms.realestatemanager.Database.Dao

import android.arch.persistence.room.Dao
import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.openclassrooms.realestatemanager.Models.Estate
import com.openclassrooms.realestatemanager.Models.FullEstate


/**
 * Created by Adrien Deguffroy on 05/10/2018.
 */

@Dao
interface EstateDao {

    @Query("SELECT Estate.*,Location.* FROM Estate INNER JOIN Location ON Estate.id = Location.estateId ")
    fun getItems(): LiveData<List<FullEstate>>

    @Query("SELECT Estate.*,Location.* FROM Estate INNER JOIN Location ON Estate.id = Location.estateId  WHERE Estate.id = :index")
    fun getItemsByID(index:Long) : LiveData<FullEstate>

    @Insert
    fun insertItem(estate: Estate) : Long

    @Update
    fun updateItem(estate: Estate)
}