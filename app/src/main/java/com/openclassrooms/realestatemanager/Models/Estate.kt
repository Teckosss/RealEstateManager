package com.openclassrooms.realestatemanager.Models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.Utils

/**
 * Created by Adrien Deguffroy on 05/10/2018.
 */

@Entity
data class Estate(@PrimaryKey(autoGenerate = true) val id: Long,
                  var estateType: String?,
                  var price: Double?,
                  var surface: Int?,
                  var roomNumber: Int?,
                  var desc: String?,
                  //var address: String?,
                  var pointInterest: String?,
                  var estateStatute: String?,
                  var entryDate: String = Utils.getTodayDate(),
                  var soldDate: String?,
                  var estateAgent: String?)