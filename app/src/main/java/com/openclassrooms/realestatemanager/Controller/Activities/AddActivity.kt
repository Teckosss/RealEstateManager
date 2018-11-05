package com.openclassrooms.realestatemanager.Controller.Activities

import android.app.Activity
import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.openclassrooms.realestatemanager.Controller.ViewModel.EstateViewModel
import com.openclassrooms.realestatemanager.Controller.Views.ActivityAddAdapter
import com.openclassrooms.realestatemanager.Di.Injection
import com.openclassrooms.realestatemanager.Models.Estate
import com.openclassrooms.realestatemanager.Models.Image
import com.openclassrooms.realestatemanager.Models.Location
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.*
import com.openclassrooms.realestatemanager.Utils.Constants.RC_CHOOSE_PHOTO
import com.openclassrooms.realestatemanager.Utils.Constants.RC_TAKE_PHOTO
import kotlinx.android.synthetic.main.custom_dialog_overlay.*
import kotlinx.android.synthetic.main.estate_info.*
import java.io.File
import java.io.IOException
import java.net.URI
import java.sql.Date
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddActivity : BaseActivity(), ActivityAddAdapter.Listener {

    private lateinit var adapter:ActivityAddAdapter
    private lateinit var images:ArrayList<Uri>
    private lateinit var uriImageSelected:Uri
    private val listImages = ArrayList<Uri>()

    private lateinit var estateViewModel:EstateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        this.estateViewModel = ViewModelProviders.of(this,Injection.provideViewModelFactory(this)).get(EstateViewModel::class.java)

        this.configureToolbar()
        this.setOnClickListener()
        this.configureRecyclerView()
        this.configureOnClickRecyclerView()
        this.populateWithTodayDate(add_activity_date)

        add_activity_check_sold.visibility = View.GONE
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    private fun configureRecyclerView() {
        this.images = ArrayList()
        this.adapter = ActivityAddAdapter(estateViewModel.listImagesToSave,this, Constants.VIEW_HOLDER_ACTION_ADD)
        this.add_activity_recycler_view.adapter = this.adapter
        this.add_activity_recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    // -------------------
    // UPDATE UI
    // -------------------

    private fun <T> updateUI(results:T) {
        if(results is Uri){
            images.add(results)
        }else if(results is ArrayList<*>){
            images.addAll(results as ArrayList<Uri>)
        }

       adapter.notifyDataSetChanged()
    }

    // --------------------
    // ACTIONS
    // --------------------

    private fun configureOnClickRecyclerView() {
        ItemClickSupport.addTo(add_activity_recycler_view, R.layout.activity_add_item)
                .setOnItemClickListener { recyclerView, position, v ->
                    Log.e("TAG", "Position : $position")
                    showOverlay(this, estateViewModel.listImagesToSave[position], position, adapter)
                }
    }

    override fun onClickDeleteButton(position: Int) {
        //images.removeAt(position)
        estateViewModel.listImagesToSave.removeAt(position)
        adapter.notifyDataSetChanged()
    }

    override fun saveEstateToDatabase(){
        var canSaveEstate = false
        if (estateViewModel.listImagesToSave.isNotEmpty()){
            for(image in estateViewModel.listImagesToSave){
                canSaveEstate = image.imageDesc != null && image.imageDesc!!.isNotEmpty() && image.imageTitle != null && image.imageTitle!!.isNotEmpty()
            }
        }else{
            canSaveEstate = true
        }

        if (canSaveEstate){
            val estate = Estate(0,
                    add_activity_spinner.text.toString(),
                    add_activity_price.text.toString().toDoubleOrNull(),
                    add_activity_surface.text.toString().toIntOrNull(),
                    add_activity_room_number.text.toString().toIntOrNull(),
                    add_activity_bathroom_number.text.toString().toIntOrNull(),
                    add_activity_bedroom_number.text.toString().toIntOrNull(),
                    add_activity_desc.text.toString(),
                    nearby_parks.isChecked,
                    nearby_shops.isChecked,
                    nearby_schools.isChecked,
                    nearby_highway.isChecked,
                    resources.getString(R.string.activity_add_estate_available),
                    add_activity_date.text.toString().toFRDate(),
                    null,
                    "Adrien")

            val location = Location(0,
                    add_activity_address.text.toString(),
                    add_activity_add_address.text.toString(),
                    add_activity_sector_address.text.toString(),
                    add_activity_city_address.text.toString(),
                    add_activity_zip_address.text.toString(),
                    add_activity_country_address.text.toString(),
                    0)

            this.estateViewModel.createEstate(estate, location,applicationContext, estateViewModel.listImagesToSave.toList())
            this.clearAllFields()
        }else{
            Toast.makeText(this, resources.getString(R.string.activity_add_estate_save_error), Toast.LENGTH_LONG).show()
        }
    }

    private fun clearAllFields(){
        add_activity_spinner.text = null
        add_activity_price.text = null
        add_activity_surface.text = null
        add_activity_room_number.text = null
        add_activity_bathroom_number.text = null
        add_activity_bedroom_number.text = null
        add_activity_desc.text = null

        add_activity_address.text = null
        add_activity_add_address.text = null
        add_activity_sector_address.text = null
        add_activity_city_address.text = null
        add_activity_zip_address.text = null
        add_activity_country_address.text = null

        nearby_parks.isChecked = false
        nearby_shops.isChecked = false
        nearby_schools.isChecked = false
        nearby_highway.isChecked = false
        estateViewModel.listImagesToSave.clear()
        adapter.notifyDataSetChanged()
    }

    // --------------------
    // FILE MANAGEMENT
    // --------------------

    //  Handle activity response (after user has chosen or not a picture)
    override fun handleResponse(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) { //SUCCESS
                if (data!!.clipData != null) {
                    listImages.clear()
                    (0 until data.clipData.itemCount ).forEach {
                        i ->
                        val uri = data.clipData.getItemAt(i).uri
                        listImages.add(uri)
                        val imageToSave = Image(0,uri.toString(),null,null,0)
                        estateViewModel.listImagesToSave.add(imageToSave)
                    }
                    updateUI(listImages)
                }else if(data.data != null) {
                    this.uriImageSelected = data.data
                    val imageToSave = Image(0,uriImageSelected.toString(),null,null,0)
                    estateViewModel.listImagesToSave.add(imageToSave)
                    updateUI(uriImageSelected)
                }
            }
        }else if(requestCode == Constants.RC_TAKE_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                val imageToSave = Image(0,photoURI.toString(),null,null,0)
                estateViewModel.listImagesToSave.add(imageToSave)
                updateUI(photoURI)
            }

        }else {
            Log.e("TAG","No pic choose")
        }
    }
}
