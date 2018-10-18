package com.openclassrooms.realestatemanager.Controller.Repositories

import android.arch.lifecycle.LiveData
import com.openclassrooms.realestatemanager.Database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.Models.Image
import io.reactivex.Observable

/**
 * Created by Adrien Deguffroy on 11/10/2018.
 */
data class ImageDataRepository(private val database: RealEstateManagerDatabase) {

    // --- GET ---

    fun getImages(estateId:Long): LiveData<List<Image>> {
        return this.database.imageDao().getItems(estateId)
    }

    // --- CREATE ---

    fun createImage(image: Image) : Observable<Long> {
        return Observable.fromCallable{database.imageDao().insertItem(image)}
    }
}