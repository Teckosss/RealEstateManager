package com.openclassrooms.realestatemanager.Models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.content.ContentValues
import android.util.Log
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.Utils
import com.openclassrooms.realestatemanager.Utils.toFRDate
import java.util.*

/**
 * Created by Adrien Deguffroy on 05/10/2018.
 */

@Entity
data class Estate(@PrimaryKey(autoGenerate = true) var id: Long,
                  var estateType: String?,
                  var price: Double?,
                  var surface: Int?,
                  var roomNumber: Int?,
                  var bathroomNumber:Int?,
                  var bedroomNumber:Int?,
                  var desc: String?,
                  var parks:Boolean,
                  var shops:Boolean,
                  var schools:Boolean,
                  var highway:Boolean,
                  var estateStatute: String?,
                  var entryDate: Date,
                  var soldDate: Date?,
                  var estateAgent: String?){

    constructor() : this(0,"",null,null,null,
            null,null,"",false,false,
            false,false,"",Date(),null,"")

    fun fromContentValues(values:ContentValues):Estate{
        val estate = Estate()
        if (values.containsKey("estateType")) estate.estateType = values.getAsString("estateType")
        if (values.containsKey("price")) estate.price = values.getAsDouble("price")
        if (values.containsKey("surface")) estate.surface = values.getAsInteger("surface")
        if (values.containsKey("roomNumber")) estate.roomNumber = values.getAsInteger("roomNumber")
        if (values.containsKey("bathroomNumber")) estate.bathroomNumber = values.getAsInteger("bathroomNumber")
        if (values.containsKey("bedroomNumber")) estate.bedroomNumber = values.getAsInteger("bedroomNumber")
        if (values.containsKey("desc")) estate.desc = values.getAsString("desc")
        if (values.containsKey("parks")) estate.parks = values.getAsBoolean("parks")
        if (values.containsKey("shops")) estate.shops = values.getAsBoolean("shops")
        if (values.containsKey("schools")) estate.schools = values.getAsBoolean("schools")
        if (values.containsKey("highway")) estate.highway = values.getAsBoolean("highway")
        if (values.containsKey("estateStatute")) estate.estateStatute = values.getAsString("estateStatute")
        if (values.containsKey("entryDate")) estate.entryDate = values.getAsLong("entryDate").toFRDate()
        if (values.containsKey("soldDate")) if (values.getAsLong("soldDate") == 0L) estate.soldDate = null else estate.soldDate = values.getAsLong("soldDate").toFRDate()
        if (values.containsKey("estateAgent")) estate.estateAgent = values.getAsString("estateAgent")
        Log.e("EstateFromContentValues", "Estate : $estate")
        return estate
    }


}