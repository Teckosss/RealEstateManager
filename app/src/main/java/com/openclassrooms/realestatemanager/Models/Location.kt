package com.openclassrooms.realestatemanager.Models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Adrien Deguffroy on 16/10/2018.
 */

@Entity(foreignKeys = [ForeignKey(entity = Estate::class,
        parentColumns = ["id"],
        childColumns = ["estateId"])])
data class Location(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "locationId") var id:Long,
                    var address:String?,
                    var additionalAddress:String?,
                    var city:String?,
                    var zipCode:String?,
                    var country:String?,
                    var estateId:Long)