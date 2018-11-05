package com.openclassrooms.realestatemanager.Controller.Activities

import android.app.Activity
import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.UserManager
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.openclassrooms.realestatemanager.Controller.Fragments.DATABASE_ID
import com.openclassrooms.realestatemanager.Controller.ViewModel.EstateViewModel
import com.openclassrooms.realestatemanager.Controller.Views.ActivityAddAdapter
import com.openclassrooms.realestatemanager.Di.Injection
import com.openclassrooms.realestatemanager.Models.Estate
import com.openclassrooms.realestatemanager.Models.FullEstate
import com.openclassrooms.realestatemanager.Models.Image
import com.openclassrooms.realestatemanager.Models.Location
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.*
import kotlinx.android.synthetic.main.custom_dialog_overlay.*
import kotlinx.android.synthetic.main.estate_info.*
import kotlinx.android.synthetic.main.toolbar.*
import java.sql.Date

class EditActivity : BaseActivity(), ActivityAddAdapter.Listener {

    private lateinit var mViewModel: EstateViewModel
    private lateinit var listImages:ArrayList<Any>
    private lateinit var adapter:ActivityAddAdapter
    private var databaseId:Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        mViewModel = ViewModelProviders.of(this,Injection.provideViewModelFactory(this)).get(EstateViewModel::class.java)

        this.configureToolbar()
        this.retrieveDatabaseId()
        this.setOnClickListener()
        this.configureRecyclerView()
        this.configureOnClickRecyclerView()
        this.populateWithTodayDate(add_activity_date)
        this.configureSoldLayout()
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    private fun configureSoldLayout(){
        add_activity_check_sold.setOnClickListener {
            if (add_activity_check_sold.isChecked){
                add_activity_date_sold_layout.visibility = View.VISIBLE
                this.populateWithTodayDate(add_activity_date_sold)
                add_activity_date_sold.setOnClickListener { displayDatePicker(add_activity_date_sold) }
            }else{
                add_activity_date_sold_layout.visibility = View.GONE
            }
        }
    }

    private fun configureRecyclerView(){
        this.listImages = ArrayList()
        this.adapter = ActivityAddAdapter(mViewModel.listImagesToSave,this, Constants.VIEW_HOLDER_ACTION_EDIT)
        add_activity_recycler_view.adapter = this.adapter
        add_activity_recycler_view.layoutManager = LinearLayoutManager(this,LinearLayout.HORIZONTAL,false)
    }

    private fun configureOnClickRecyclerView(){
        ItemClickSupport.addTo(add_activity_recycler_view,R.layout.activity_add_item).setOnItemClickListener{
            recyclerView: RecyclerView?, position: Int, v: View? ->
            showOverlay(this, mViewModel.listImagesToSave[position], position,adapter)
        }
    }

    private fun retrieveDatabaseId(){
        databaseId = intent.extras[DATABASE_ID] as Long
        //Log.e("EDIT_ACTIVITY","Extra : $databaseId")
        if (databaseId > 0){
            mViewModel.getEstatesByID(databaseId).observe(this, Observer { updateUI(it!!) })
        }
    }

    // ---------------------
    // ACTION
    // ---------------------

    override fun onClickDeleteButton(position: Int) {
        mViewModel.listImagesToDeleteFromDB.add(mViewModel.listImagesToSave[position])
        mViewModel.listImagesToSave.removeAt(position)
        adapter.notifyDataSetChanged()
    }

    override fun saveEstateToDatabase() {
        var canSaveEstate = false
        if(mViewModel.listImagesToSave.isNotEmpty() ){
            for (image in mViewModel.listImagesToSave) {
                canSaveEstate = image.imageDesc != null && image.imageDesc!!.isNotEmpty() && image.imageTitle != null && image.imageTitle!!.isNotEmpty()
            }
        }else{
            canSaveEstate = true
        }

        if (canSaveEstate) {
            val soldDate = if(add_activity_check_sold.isChecked && !add_activity_date_sold.text.toString().isEmpty()) add_activity_date_sold.text.toString() else null
            val available = if (soldDate != null) resources.getString(R.string.activity_add_estate_sold) else resources.getString(R.string.activity_add_estate_available)
            val estate = Estate(databaseId,
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
                    available,
                    add_activity_date.text.toString().toFRDate(),
                    soldDate?.toFRDate(),
                    "Adrien")

            val location = Location(0,
                    add_activity_address.text.toString(),
                    add_activity_add_address.text.toString(),
                    add_activity_sector_address.text.toString(),
                    add_activity_city_address.text.toString(),
                    add_activity_zip_address.text.toString(),
                    add_activity_country_address.text.toString(),
                    databaseId)

            this.mViewModel.updateEstate(estate, location, applicationContext, mViewModel.listImagesToSave.toList(), mViewModel.listImagesToDeleteFromDB)
        }else{
            Toast.makeText(this, resources.getString(R.string.activity_add_estate_save_error), Toast.LENGTH_LONG).show()
        }
    }

    // ---------------------
    // UI
    // ---------------------

    private fun <T> updateUI(result:T){
        if (result is FullEstate){
            mViewModel.listImagesToSave.clear()
            if (result.estate.estateStatute == resources.getString(R.string.activity_add_estate_sold)){
                add_activity_check_sold.isChecked = true
                add_activity_date_sold_layout.visibility = View.VISIBLE
                this.retrieveTextAndPopulateEditText(add_activity_date_sold, result.estate.soldDate?.toFRString())
            }

            if (result.estate.parks) nearby_parks.isChecked = true
            if (result.estate.shops) nearby_shops.isChecked = true
            if (result.estate.schools) nearby_schools.isChecked = true
            if (result.estate.highway) nearby_highway.isChecked = true

            this.retrieveTextAndPopulateEditText(add_activity_spinner,result.estate.estateType)
            this.retrieveTextAndPopulateEditText(add_activity_price,result.estate.price.toString())
            this.retrieveTextAndPopulateEditText(add_activity_surface,result.estate.surface.toString())
            this.retrieveTextAndPopulateEditText(add_activity_room_number,result.estate.roomNumber.toString())
            this.retrieveTextAndPopulateEditText(add_activity_bathroom_number,result.estate.bathroomNumber.toString())
            this.retrieveTextAndPopulateEditText(add_activity_bedroom_number,result.estate.bedroomNumber.toString())
            this.retrieveTextAndPopulateEditText(add_activity_desc,result.estate.desc)
            this.retrieveTextAndPopulateEditText(add_activity_date,result.estate.entryDate.toFRString())

            this.retrieveTextAndPopulateEditText(add_activity_address,result.location.address)
            this.retrieveTextAndPopulateEditText(add_activity_add_address,result.location.additionalAddress)
            this.retrieveTextAndPopulateEditText(add_activity_sector_address,result.location.sector)
            this.retrieveTextAndPopulateEditText(add_activity_city_address,result.location.city)
            this.retrieveTextAndPopulateEditText(add_activity_zip_address,result.location.zipCode)
            this.retrieveTextAndPopulateEditText(add_activity_country_address,result.location.country)


            mViewModel.listImagesToSave.addAll(result.images)

        }else if (result is Uri){
            listImages.add(result)
        }

        adapter.notifyDataSetChanged()
    }

    private fun retrieveTextAndPopulateEditText(view:TextInputEditText, data:String?){
        Log.e("Edit_RETRIEVE","Data : $data")
        if (data != "null" && data != null){
            view.setText(data.toString(),TextView.BufferType.EDITABLE)
        }else{
            view.setText("",TextView.BufferType.EDITABLE)
        }
    }

    // ---------------------
    // FILE MANAGEMENT
    // ---------------------

    override fun handleResponse(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) { //SUCCESS
                if (data!!.clipData != null) {
                    listImages.clear()
                    (0 until data.clipData.itemCount).forEach { i ->
                        val uri = data.clipData.getItemAt(i).uri
                        listImages.add(uri)
                        val imageToSave = Image(0, uri.toString(), null, null, databaseId)
                        mViewModel.listImagesToSave.add(imageToSave)
                    }
                    Log.e("HandleResponse", "ListImages : $listImages")
                    updateUI(listImages)
                    //do something with the image (save it to some directory or whatever you need to do with it here)
                } else if (data.data != null) {
                    val uriImageSelected = data.data
                    val imageToSave = Image(0, uriImageSelected.toString(), null, null, databaseId)
                    mViewModel.listImagesToSave.add(imageToSave)
                    updateUI(uriImageSelected)
                    //do something with the image (save it to some directory or whatever you need to do with it here)
                }
            }
        } else if (requestCode == Constants.RC_TAKE_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                val imageToSave = Image(0, photoURI.toString(), null, null, databaseId)
                mViewModel.listImagesToSave.add(imageToSave)
                updateUI(photoURI)
            } else {
                Log.e("TAG", "No pic choose")
            }
        }
    }
}
