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
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.openclassrooms.realestatemanager.Controller.Activities.MainActivity
import com.openclassrooms.realestatemanager.Controller.ViewModel.EstateViewModel
import com.openclassrooms.realestatemanager.Di.Injection

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import kotlinx.android.synthetic.main.fragment_map.*


/**
 * A simple [Fragment] subclass.
 *
 */

class MapFragment : Fragment(), GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private lateinit var mViewModel: EstateViewModel
    private var mMap: GoogleMap? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var mLocationRequest : LocationRequest
    private lateinit var mLocationCallback : LocationCallback

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

        mViewModel.getEstates().observe(this, Observer { })

        map_fragment.onCreate(savedInstanceState)
        map_fragment.onResume()

        this.configureMap()
        this.configureGoogleApiClient()
        this.configureLocationRequest()
        this.configureLocationCallback()
    }

    override fun onResume() {
        super.onResume()
        map_fragment.onResume()
        if (mGoogleApiClient != null) mGoogleApiClient?.connect()
    }

    override fun onPause() {
        super.onPause()
        map_fragment.onPause()
        if (mGoogleApiClient != null && mGoogleApiClient?.isConnected!!){
            this.stopLocationUpdate()
            mGoogleApiClient?.stopAutoManage(activity!!)
            mGoogleApiClient?.disconnect()
        }
    }

    override fun onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu()
        map_fragment.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        map_fragment.onLowMemory()
    }

    // -----------------
    // CONFIGURATION
    // -----------------

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
            it.isMyLocationEnabled = true
            it.uiSettings.isCompassEnabled = true
            val locationButton = (map_fragment.findViewById<View>(Integer.parseInt("1")).parent as View).findViewById<View>(Integer.parseInt("2"))
            // and next place it, for example, on bottom right (as Google Maps app)
            val rlp = locationButton.layoutParams as RelativeLayout.LayoutParams
            // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
            rlp.setMargins(0, 0, 30, 30)
            it.uiSettings.isMyLocationButtonEnabled = true
            it.uiSettings.isRotateGesturesEnabled = true
            it.setOnMarkerClickListener { this.onClickMarker() }
        }
    }

    private fun configureGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient
                .Builder(this.context!!)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .enableAutoManage(activity!!, this)
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

    private fun onClickMarker(): Boolean {
        return true
    }

    override fun onLocationChanged(p0: Location?) { this.handleNewLocation(p0!!) }
    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {}
    override fun onProviderEnabled(p0: String?) {}
    override fun onProviderDisabled(p0: String?) {}
}
