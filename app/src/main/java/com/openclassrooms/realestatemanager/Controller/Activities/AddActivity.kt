package com.openclassrooms.realestatemanager.Controller.Activities

import android.Manifest
import android.app.DatePickerDialog
import android.app.PendingIntent.getActivity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnItemSelected
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.openclassrooms.realestatemanager.Controller.Views.ActivityAddAdapter
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.ItemClickSupport
import com.openclassrooms.realestatemanager.Utils.Utils
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.android.synthetic.main.toolbar.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.net.URI
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        ButterKnife.bind(this)

        this.configureToolbar()
        this.populateSpinner()
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

    private fun populateSpinner(){
        ArrayAdapter.createFromResource(
                this,
                R.array.estate_type,
                android.R.layout.simple_spinner_item
        ).also{
            adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
            spinner.adapter = adapter
        }
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
                .setOnItemClickListener { recyclerView, position, v -> Log.e("TAG", "Position : $position") }
    }

    override fun onClickDeleteButton(position: Int) {
        images.removeAt(position)
        adapter.notifyDataSetChanged()
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
