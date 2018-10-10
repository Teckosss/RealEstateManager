package com.openclassrooms.realestatemanager.Models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.openclassrooms.realestatemanager.Utils.Utils

/**
 * Created by Adrien Deguffroy on 05/10/2018.
 */

@Entity
data class Estate(@PrimaryKey(autoGenerate = true) private val id: Long,
                  var estateType: EstateType?,
                  var price: Double?,
                  var surface: Int?,
                  var roomNumber: Int?,
                  var desc: String?,
                  var address: String?,
                  var pointInterest: String?,
                  var estateStatute: EstateStatute = EstateStatute.AVAILABLE,
                  var entryDate: String = Utils.getTodayDate(),
                  var soldDate: String?,
                  var estateAgent: String?)

enum class EstateType(val value:Int){ FLAT(0),HOUSE(1),DUPLEX(2),PENTHOUSE(3) }

enum class EstateStatute{AVAILABLE,SOLD}