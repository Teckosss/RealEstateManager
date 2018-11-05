package com.openclassrooms.realestatemanager.Database.Converters

import android.arch.persistence.room.TypeConverter
import java.util.*

/**
 * Created by Adrien Deguffroy on 05/11/2018.
 */
class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = if (value == null) null else Date(value)

    @TypeConverter fun dateToTimestamp(date: Date?): Long? = date?.time
}