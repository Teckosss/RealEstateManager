package com.openclassrooms.realestatemanager.Models

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Relation

/**
 * Created by Adrien Deguffroy on 22/10/2018.
 */
class FullEstate {

    @Embedded
    lateinit var estate:Estate

    @Relation(parentColumn = "id",
            entityColumn = "estateId")
    var images:List<Image> = arrayListOf()

    @Embedded
    lateinit var location:Location
}