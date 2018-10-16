package com.openclassrooms.realestatemanager.Controller.Repositories

import android.arch.lifecycle.LiveData
import com.openclassrooms.realestatemanager.Database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.Models.Location
import io.reactivex.Observable

/**
 * Created by Adrien Deguffroy on 16/10/2018.
 */
class LocationDataRepository(private val database: RealEstateManagerDatabase) {

    // --- GET ---

    fun getLocation(): LiveData<List<Location>> {
        return this.database.locationDao().getItems()
    }

    // --- CREATE ---

    fun createLocation(location: Location) : Observable<Long> {
        return Observable.fromCallable{database.locationDao().insertItem(location)}
    }
}