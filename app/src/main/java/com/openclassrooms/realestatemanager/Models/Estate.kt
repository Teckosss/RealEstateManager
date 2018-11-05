package com.openclassrooms.realestatemanager.Models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.Utils
import java.util.*

/**
 * Created by Adrien Deguffroy on 05/10/2018.
 */

@Entity
data class Estate(@PrimaryKey(autoGenerate = true) val id: Long,
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
                  var estateAgent: String?)