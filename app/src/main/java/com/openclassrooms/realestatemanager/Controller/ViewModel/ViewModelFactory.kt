package com.openclassrooms.realestatemanager.Controller.ViewModel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.openclassrooms.realestatemanager.Controller.Repositories.EstateDataRepository
import com.openclassrooms.realestatemanager.Controller.Repositories.ImageDataRepository
import com.openclassrooms.realestatemanager.Controller.Repositories.LocationDataRepository
import io.reactivex.Scheduler
import java.util.concurrent.Executor


/**
 * Created by Adrien Deguffroy on 15/10/2018.
 */
class ViewModelFactory(private val estateDataRepository: EstateDataRepository,
                       private val imageDataRepository: ImageDataRepository,
                       private val locationDataRepository: LocationDataRepository,
                       private val subscriberOn: Scheduler,
                       private val observerOn: Scheduler) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EstateViewModel::class.java!!)) {
            return EstateViewModel(estateDataRepository,imageDataRepository,locationDataRepository,subscriberOn,observerOn) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}