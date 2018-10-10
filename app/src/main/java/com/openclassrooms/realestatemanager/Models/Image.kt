package com.openclassrooms.realestatemanager.Models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Adrien Deguffroy on 10/10/2018.
 */

@Entity(foreignKeys = [ForeignKey(entity = Estate::class,
        parentColumns = ["id"],
        childColumns = ["estateId"])])
data class Image(@PrimaryKey(autoGenerate = true) private val id:Long,
                 var imagePath:String,
                 var imageDesc:String,
                 private var estateId:Long
                 )