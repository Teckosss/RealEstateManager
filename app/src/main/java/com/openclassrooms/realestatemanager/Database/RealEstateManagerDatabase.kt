package com.openclassrooms.realestatemanager.Database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.openclassrooms.realestatemanager.Database.Dao.EstateDao
import com.openclassrooms.realestatemanager.Database.Dao.ImageDao
import com.openclassrooms.realestatemanager.Database.Dao.LocationDao
import com.openclassrooms.realestatemanager.Models.Estate
import com.openclassrooms.realestatemanager.Models.Image
import com.openclassrooms.realestatemanager.Models.Location

/**
 * Created by Adrien Deguffroy on 05/10/2018.
 */

@Database(entities = [(Estate::class), (Image::class), (Location::class)],version = 1, exportSchema = false)
abstract class RealEstateManagerDatabase : RoomDatabase() {

    abstract fun estateDao():EstateDao
    abstract fun imageDao():ImageDao
    abstract fun locationDao():LocationDao

    companion object {
        private var INSTANCE: RealEstateManagerDatabase? = null

        fun getInstance(context: Context):RealEstateManagerDatabase{
            if (INSTANCE == null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,RealEstateManagerDatabase::class.java,"RealEstateManager.db").build()
                }
            }
            return INSTANCE as RealEstateManagerDatabase
        }

        fun destroyInstance(){
            INSTANCE = null
        }
    }
}