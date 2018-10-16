package com.openclassrooms.realestatemanager.Controller.Activities

import android.Manifest
import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.location.Address
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupMenu
import butterknife.BindView
import butterknife.ButterKnife
import com.openclassrooms.realestatemanager.Controller.ViewModel.EstateViewModel
import com.openclassrooms.realestatemanager.Controller.Views.ActivityAddAdapter
import com.openclassrooms.realestatemanager.Di.Injection
import com.openclassrooms.realestatemanager.Models.Estate
import com.openclassrooms.realestatemanager.Models.Location
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.ItemClickSupport
import com.openclassrooms.realestatemanager.Utils.Utils
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.toolbar.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val PERMS = Manifest.permission.READ_EXTERNAL_STORAGE
private const val RC_IMAGE_PERMS = 100
private const val RC_CHOOSE_PHOTO = 200

class AddActivity : AppCompatActivity(), ActivityAddAdapter.Listener {

    private lateinit var adapter:ActivityAddAdapter
    private lateinit var images:ArrayList<Uri>
    private lateinit var uriImageSelected:Uri

    private lateinit var estateViewModel:EstateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        ButterKnife.bind(this)

        this.estateViewModel = ViewModelProviders.of(this,Injection.provideViewModelFactory(this)).get(EstateViewModel::class.java)

        this.configureToolbar()
        this.setOnClickListener()
        this.populateWithTodayDate()
        this.configureRecyclerView()
        this.configureOnClickRecyclerView()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        this.handleResponse(requestCode,resultCode,data)

    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    private fun configureToolbar(){
        setSupportActionBar(simple_toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun configureRecyclerView() {
        this.images = ArrayList()
        this.adapter = ActivityAddAdapter(this.images,this)
        this.add_activity_recycler_view.adapter = this.adapter
        this.add_activity_recycler_view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setOnClickListener(){
        add_activity_date.setOnClickListener{this.displayDatePicker()}
        add_activity_choose_pic.setOnClickListener{this.onClickAddFile()}
        add_activity_spinner.setOnClickListener{this.displayPopupMenu()}
        add_activity_save.setOnClickListener{this.saveEstateToDatabase()}
    }

    // -------------------
    // UPDATE UI
    // -------------------

    private fun <T> updateUI(results:T) {
        if(results is Uri){
            Log.e("UpdateUI","List : $results")
            images.add(results)
        }else if(results is ArrayList<*>){
            images.addAll(results as ArrayList<Uri>)
        }

        adapter.notifyDataSetChanged()
    }

    private fun displayDatePicker(){
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val calendar:Calendar = Calendar.getInstance()
        val mYear = calendar.get(Calendar.YEAR)
        val mMonth = calendar.get(Calendar.MONTH)
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{view,year,monthOfYear,dayOfMonth ->
            add_activity_date.setText(sdf.format(Date(year - 1900,monthOfYear,dayOfMonth)))

        }, mYear,mMonth,mDay)
        datePickerDialog.show()
    }

    private fun displayPopupMenu(){
        val popupMenu = PopupMenu(this,add_activity_spinner)
        popupMenu.menuInflater.inflate(R.menu.popup_menu_estate_type,popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item -> add_activity_spinner.setText(item.title); true}
        popupMenu.show()
    }

    private fun populateWithTodayDate(){add_activity_date.setText(Utils.getTodayDate())}

    // --------------------
    // ACTIONS
    // --------------------

    @AfterPermissionGranted(RC_IMAGE_PERMS)
    private fun onClickAddFile() {
        if (!EasyPermissions.hasPermissions(this,PERMS)) {
            EasyPermissions.requestPermissions(this, getString(R.string.popup_title_permission_files_access), RC_IMAGE_PERMS, PERMS)
            return
        }else
            this.chooseImageFromPhone()
    }

    private fun configureOnClickRecyclerView() {
        ItemClickSupport.addTo(add_activity_recycler_view, R.layout.activity_add_item)
                .setOnItemClickListener { recyclerView, position, v ->
                    Log.e("TAG", "Position : $position")
                    //createDescBlock()
                }
    }

    override fun onClickDeleteButton(position: Int) {
        images.removeAt(position)
        adapter.notifyDataSetChanged()
    }

    private fun createDescBlock(){
        val textInputLayout = TextInputLayout(this)
        textInputLayout.id = View.generateViewId()
        val textInputLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        textInputLayout.layoutParams = textInputLayoutParams

        val textInputEditText = TextInputEditText(textInputLayout.context)
        textInputEditText.id = View.generateViewId()
        val editTextParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        textInputEditText.layoutParams = editTextParams

        textInputLayout.addView(textInputEditText,editTextParams)

        add_activity_container.addView(textInputLayout, textInputLayoutParams)
    }

    private fun saveEstateToDatabase(){
        val estate = Estate(0,
                add_activity_spinner.text.toString(),
                add_activity_price.text.toString().toDoubleOrNull(),
                add_activity_surface.text.toString().toIntOrNull(),
                add_activity_room_number.text.toString().toIntOrNull(),
                add_activity_desc.text.toString(),
                null,
                resources.getString(R.string.activity_add_estate_available),
                Utils.getTodayDate(),
                null,
                "Adrien")
       estateViewModel.createEstate(estate)


    }

    fun saveLocationToDatabase(estateId:Long){
        Log.e("ADD_ACTIVITY","Id Inserted is : $estateId")

        val location = Location(0,
                add_activity_address.text.toString(),
                add_activity_add_address.text.toString(),
                add_activity_city_address.text.toString(),
                add_activity_zip_address.text.toString(),
                add_activity_country_address.text.toString(),
                estateId)

        Log.e("Location","Info : $location")

        estateViewModel.createLocation(location)
    }

// --------------------
    // FILE MANAGEMENT
    // --------------------

    private fun chooseImageFromPhone() {
        if (!EasyPermissions.hasPermissions(this, PERMS)) {
            EasyPermissions.requestPermissions(this, getString(R.string.popup_title_permission_files_access), RC_IMAGE_PERMS, PERMS)
            return
        }
        // Launch an "Selection Image" Activity
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*" //allows any image file type. Change * to specific extension to limit it
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), RC_CHOOSE_PHOTO)
    }

    //  Handle activity response (after user has chosen or not a picture)
    private fun handleResponse(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) { //SUCCESS
                if (data!!.clipData != null) {
                    val listImages = ArrayList<Uri>()
                    (0 until data.clipData.itemCount ).forEach { i -> listImages.add(data.clipData.getItemAt(i).uri) }
                    Log.e("HandleResponse","ListImages : $listImages")
                    updateUI(listImages)
                    //do something with the image (save it to some directory or whatever you need to do with it here)
                }else if(data.data != null) {
                    this.uriImageSelected = data.data
                    updateUI(this.uriImageSelected)
                    //do something with the image (save it to some directory or whatever you need to do with it here)
                }
            }
        } else {
            Log.e("TAG","No pic choose")
        }
    }
}
