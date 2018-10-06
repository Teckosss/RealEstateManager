package com.openclassrooms.realestatemanager.Models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.openclassrooms.realestatemanager.Utils
import java.util.*

/**
 * Created by Adrien Deguffroy on 05/10/2018.
 */

@Entity
class Estate(@PrimaryKey(autoGenerate = true) private val id: Long,
             var estateType: EstateType,
             var price: Double,
             var surface: Int,
             var roomNumber: Int,
             var desc: String,
             var address: String,
             var pointInterest: String,
             var estateStatut: EstateStatute = EstateStatute.AVAILABLE,
             var entryDate: String = Utils.getTodayDate(),
             var soldDate: String,
             var estateAgent: String)

enum class EstateType{FLAT,HOUSE,DUPLEX,PENTHOUSE}

enum class EstateStatute{AVAILABLE,SOLD}