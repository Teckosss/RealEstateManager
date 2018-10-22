package com.openclassrooms.realestatemanager.Models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

/**
 * Created by Adrien Deguffroy on 10/10/2018.
 */

@Entity(foreignKeys = [ForeignKey(entity = Estate::class,
        parentColumns = ["id"],
        childColumns = ["estateId"])])
data class Image(@PrimaryKey(autoGenerate = true) val id:Long,
                 var imagePath: String,
                 var imageTitle:String?,
                 var imageDesc:String?,
                 var estateId:Long
                 )