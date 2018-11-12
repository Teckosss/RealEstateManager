package com.openclassrooms.realestatemanager.Controller.Fragments

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.openclassrooms.realestatemanager.Controller.Activities.MainActivity
import com.openclassrooms.realestatemanager.Controller.ViewModel.EstateViewModel
import com.openclassrooms.realestatemanager.Di.Injection

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.gson.GsonBuilder
import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.openclassrooms.realestatemanager.Models.FullEstate
import com.openclassrooms.realestatemanager.Models.GeocodeInfo
import com.openclassrooms.realestatemanager.Utils.GeocodeStream
import com.openclassrooms.realestatemanager.Utils.Utils
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import kotlinx.android.synthetic.main.fragment_map.*


/**
 * A simple [Fragment] subclass.
 *
 */

class MapFragment : BaseFragment(), GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private lateinit var mViewModel: EstateViewModel
    private var mMap: GoogleMap? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mLocationRequest : LocationRequest
    private lateinit var mLocationCallback : LocationCallback
    private var listFullEstate = ArrayList<FullEstate>()

    companion object {
        fun newInstance(): MapFragment {
            return MapFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        mViewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this.context!!)).get(EstateViewModel::class.java)

        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Utils{ internet ->
            if (internet){
                mViewModel.getEstates().observe(this, Observer {
                    if (it != null) {
                        listFullEstate.addAll(it)
                        retrieveAddressByGeoCoding(it)
                    }
                })
            }else{
                Toast.makeText(context,resources.getString(R.string.exception_error_message),Toast.LENGTH_SHORT).show()
            }
        }



        map_fragment.onCreate(savedInstanceState)
        map_fragment.onResume()

        this.configureMap()
        this.configureGoogleApiClient()
        this.configureLocationRequest()
        this.configureLocationCallback()
    }

    override fun onStart() {
        super.onStart()
        if(!mGoogleApiClient?.isConnected!! && !mGoogleApiClient?.isConnecting!!){
            mGoogleApiClient?.connect()
        }
    }

    override fun onResume() {
        super.onResume()
        map_fragment.onResume()
        if (mGoogleApiClient != null) mGoogleApiClient?.connect()
    }

    override fun onPause() {
        super.onPause()
        map_fragment.onPause()
        this.stopLocationUpdateAndDisconnectGoogleApiClient()
    }

    override fun onStop() {
        super.onStop()
        this.stopLocationUpdateAndDisconnectGoogleApiClient()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.disposeWhenDestroy()
        this.stopLocationUpdateAndDisconnectGoogleApiClient()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_fragment.onLowMemory()
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    private fun stopLocationUpdateAndDisconnectGoogleApiClient(){
        if (mGoogleApiClient != null){
            Log.e("MAP_FRAGMENT", "ENTER IF CONDITION")
            this.stopLocationUpdate()
            mGoogleApiClient?.stopAutoManage(activity!!)
            mGoogleApiClient?.disconnect()
        }
    }

    private fun retrieveAddressByGeoCoding(listEstate: List<FullEstate>){
        (0 until listEstate.size).forEach {
            val address = getFullAddressFromEstate(listEstate[it].location)
            if (address != null){ Log.e("MAP_FRAGMENT","Address : $address")
                this.executeHttpRequestWithRetrofit(address, listEstate[it].estate.id)} else Log.e("MAP_FRAGMENT","Address error")
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) { Log.e("MAP_FRAGMENT","Connection result : $p0") }

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        mFusedLocationClient.lastLocation.addOnSuccessListener {
            if (it != null){
                this.handleNewLocation(it)
            }else{
                mFusedLocationClient.requestLocationUpdates(mLocationRequest,mLocationCallback,null)
            }
        }
    }
    override fun onConnectionSuspended(p0: Int) {}

    @SuppressLint("MissingPermission")
    private fun configureMap() {

        try {
            MapsInitializer.initialize(activity!!.baseContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        map_fragment.getMapAsync {
            mMap = it
            it.isIndoorEnabled = false
            it.isMyLocationEnabled = true
            it.uiSettings.isCompassEnabled = true
            if (map_fragment != null){
                val locationButton : View? = (map_fragment.findViewById<View>(Integer.parseInt("1")).parent as View).findViewById<View>(Integer.parseInt("2")) ?: null
                // and next place it, for example, on bottom right (as Google Maps app)
                val rlp = locationButton?.layoutParams as RelativeLayout.LayoutParams
                // position on right bottom
                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
                rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
                rlp.setMargins(0, 0, 30, 30)
            }

            it.uiSettings.isMyLocationButtonEnabled = true
            it.uiSettings.isRotateGesturesEnabled = true
            it.setOnMarkerClickListener { this.onClickMarker(it) }
        }
    }

    private fun configureGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient
                .Builder(this.context!!)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .enableAutoManage(activity!!,this)
                .build()
    }

    private fun configureLocationRequest(){
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(100 * 1000)
                .setFastestInterval(1000)
    }

    private fun configureLocationCallback(){
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    return
                }
                for (location in locationResult.locations) {
                    handleNewLocation(location)
                }
            }
        }
    }

    private fun handleNewLocation(location:Location){
        val currentLocation = LatLng(location.latitude,location.longitude)

        mMap?.moveCamera(CameraUpdateFactory.newLatLng(currentLocation))
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, Constants.MAP_FRAGMENT_DEFAULT_ZOOM))
        this.stopLocationUpdate()
    }

    private fun stopLocationUpdate(){
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
    }

    override fun onLocationChanged(p0: Location?) { this.handleNewLocation(p0!!) }
    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}
    override fun onProviderEnabled(p0: String?) {}
    override fun onProviderDisabled(p0: String?) {}

    // -------------------
    // UI
    // -------------------

    override fun getPositionFromRxRequest(position: LatLng?, estateId: Long) {
        val markerOptions = MarkerOptions()
        if (position != null){
            Log.e("MAP_FRAGMENT","Location LatLng : $position")
            markerOptions.position(position)
            markerOptions.icon(getMarkerIconFromDrawable())
            val marker = mMap?.addMarker(markerOptions)
            marker?.tag = estateId
        }
    }

    // -----------------
    // ACTION
    // -----------------

    private fun onClickMarker(marker: Marker): Boolean {
        return if (marker.tag != null && marker.tag is Long){
            Log.e("MAP_FRAGMENT","OnClickMarker tag : ${marker.tag}")
            this.launchDetailFragment(marker.tag as Long)
            true
        }else{
            Log.e("MAP_FRAGMENT","OnClickMarker tag : ERROR NO TAG")
            false
        }
    }

    private fun launchDetailFragment(databaseId : Long){
        val bundle = Bundle()
        val newFragment = DetailFragment.newInstance()
        bundle.putLong(DATABASE_ID,databaseId)
        newFragment.arguments = bundle

        val transaction = activity!!.supportFragmentManager.beginTransaction()

        if((activity as MainActivity).isTablet()){
            transaction.replace(R.id.fragment_view_detail, newFragment)
        }else{
            transaction.replace(R.id.fragment_view, newFragment)
        }

        transaction.addToBackStack(null)
        transaction.commit()

    }
}
