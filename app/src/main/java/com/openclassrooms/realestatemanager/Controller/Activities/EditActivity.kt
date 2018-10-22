package com.openclassrooms.realestatemanager.Controller.Activities

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.TextView
import com.openclassrooms.realestatemanager.Controller.Fragments.DATABASE_ID
import com.openclassrooms.realestatemanager.Controller.ViewModel.EstateViewModel
import com.openclassrooms.realestatemanager.Di.Injection
import com.openclassrooms.realestatemanager.Models.Estate
import com.openclassrooms.realestatemanager.Models.FullEstate
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.estate_info.*
import kotlinx.android.synthetic.main.fragment_list_item.*
import kotlinx.android.synthetic.main.toolbar.*

class EditActivity : AppCompatActivity() {

    private lateinit var mViewModel: EstateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        mViewModel = ViewModelProviders.of(this,Injection.provideViewModelFactory(this)).get(EstateViewModel::class.java)

        this.configureToolbar()
        this.retrieveDatabaseId()
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    private fun configureToolbar(){
        setSupportActionBar(simple_toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun retrieveDatabaseId(){
        val databaseId = intent.extras[DATABASE_ID] as Long
        Log.e("EDIT_ACTIVITY","Extra : $databaseId")
        if (databaseId > 0){
            mViewModel.getEstatesByID(databaseId).observe(this, Observer { updateUI(it!!) })
        }
    }

    // ---------------------
    // UI
    // ---------------------

    private fun updateUI(result:FullEstate){
        this.retrieveTextAndPopulateEditText(add_activity_spinner,result.estate.estateType)
        this.retrieveTextAndPopulateEditText(add_activity_price,result.estate.price.toString())
        this.retrieveTextAndPopulateEditText(add_activity_surface,result.estate.surface.toString())
        this.retrieveTextAndPopulateEditText(add_activity_room_number,result.estate.roomNumber.toString())
        this.retrieveTextAndPopulateEditText(add_activity_bathroom_number,result.estate.bathroomNumber.toString())
        this.retrieveTextAndPopulateEditText(add_activity_bedroom_number,result.estate.bedroomNumber.toString())
        this.retrieveTextAndPopulateEditText(add_activity_desc,result.estate.desc)
        this.retrieveTextAndPopulateEditText(add_activity_date,result.estate.entryDate)

        this.retrieveTextAndPopulateEditText(add_activity_address,result.location.address)
        this.retrieveTextAndPopulateEditText(add_activity_add_address,result.location.additionalAddress)
        this.retrieveTextAndPopulateEditText(add_activity_city_address,result.location.city)
        this.retrieveTextAndPopulateEditText(add_activity_zip_address,result.location.zipCode)
        this.retrieveTextAndPopulateEditText(add_activity_country_address,result.location.country)
    }

    private fun retrieveTextAndPopulateEditText(view:TextInputEditText, data:String?){
        Log.e("Edit_RETRIEVE","Data : $data")
        if (data != "null" && data != null){
            view.setText(data.toString(),TextView.BufferType.EDITABLE)
        }else{
            view.setText("",TextView.BufferType.EDITABLE)
        }

    }
}
