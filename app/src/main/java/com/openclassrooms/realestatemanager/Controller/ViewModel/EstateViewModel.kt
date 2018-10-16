package com.openclassrooms.realestatemanager.Controller.ViewModel

import android.app.Application
import android.arch.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.Controller.Repositories.EstateDataRepository
import com.openclassrooms.realestatemanager.Database.RealEstateManagerDatabase
import android.content.ClipData.Item
import android.arch.lifecycle.LiveData
import android.util.Log
import com.openclassrooms.realestatemanager.Controller.Activities.AddActivity
import com.openclassrooms.realestatemanager.Controller.Repositories.ImageDataRepository
import com.openclassrooms.realestatemanager.Controller.Repositories.LocationDataRepository
import com.openclassrooms.realestatemanager.Models.Estate
import com.openclassrooms.realestatemanager.Models.Location
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable


/**
 * Created by Adrien Deguffroy on 13/10/2018.
 */
class EstateViewModel(private val estateDataRepository: EstateDataRepository,
                      private val imageDataRepository: ImageDataRepository,
                      private val locationDataRepository: LocationDataRepository,
                      private val subscriberOn: Scheduler,
                      private val observerOn:Scheduler) : ViewModel() {

    private val disposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        this.disposable.dispose()
    }

    // --------------------
    // ESTATE
    // --------------------

    fun getEstates(): LiveData<List<Estate>> {
        return estateDataRepository.getEstates()
    }

    fun createEstate(estate:Estate) {
        this.disposable.add(estateDataRepository.createEstate(estate)
                .observeOn(observerOn)
                .subscribeOn(subscriberOn)
                .subscribe(
                        {id -> AddActivity().saveLocationToDatabase(id); Log.e("CREATE_ESTATE","OnNext")},
                        {e -> Log.e("CREATE_ESTATE","OnError : ${e.localizedMessage}")}
                )
        )
    }

    // --------------------
    // LOCATION
    // --------------------

    fun getLocation(): LiveData<List<Location>>{
        return locationDataRepository.getLocation()
    }

    fun createLocation(location: Location){
        this.disposable.add(locationDataRepository.createLocation(location)
                .observeOn(observerOn)
                .subscribeOn(subscriberOn)
                .subscribe({Log.e("CREATE_LOCATION","OnNext")},{e -> Log.e("CREATE_LOCATION","OnError : ${e.localizedMessage}")})
        )
    }
}