package com.openclassrooms.realestatemanager.Controller.Activities

import android.app.Dialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.openclassrooms.realestatemanager.Controller.Fragments.DATABASE_ID
import com.openclassrooms.realestatemanager.Controller.ViewModel.EstateViewModel
import com.openclassrooms.realestatemanager.Controller.Views.ActivityAddAdapter
import com.openclassrooms.realestatemanager.Di.Injection
import com.openclassrooms.realestatemanager.Models.Estate
import com.openclassrooms.realestatemanager.Models.FullEstate
import com.openclassrooms.realestatemanager.Models.Image
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.ItemClickSupport
import kotlinx.android.synthetic.main.custom_dialog_overlay.*
import kotlinx.android.synthetic.main.estate_info.*
import kotlinx.android.synthetic.main.fragment_list_item.*
import kotlinx.android.synthetic.main.toolbar.*

class EditActivity : AppCompatActivity(), ActivityAddAdapter.Listener {

    private lateinit var mViewModel: EstateViewModel
    private lateinit var listImages:ArrayList<Image>
    private lateinit var adapter:ActivityAddAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        mViewModel = ViewModelProviders.of(this,Injection.provideViewModelFactory(this)).get(EstateViewModel::class.java)

        this.configureToolbar()
        this.retrieveDatabaseId()
        this.configureRecyclerView()
        this.configureOnClickRecyclerView()
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    private fun configureToolbar(){
        setSupportActionBar(simple_toolbar)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun configureRecyclerView(){
        this.listImages = ArrayList()
        this.adapter = ActivityAddAdapter(this.listImages,this, VIEWHOLDER_ACTION_EDIT)
        add_activity_recycler_view.adapter = this.adapter
        add_activity_recycler_view.layoutManager = LinearLayoutManager(this,LinearLayout.HORIZONTAL,false)
    }

    private fun configureOnClickRecyclerView(){
        ItemClickSupport.addTo(add_activity_recycler_view,R.layout.activity_add_item).setOnItemClickListener{
            recyclerView: RecyclerView?, position: Int, v: View? ->
            showOverlay(this,listImages[position],position)
        }
    }

    private fun retrieveDatabaseId(){
        val databaseId = intent.extras[DATABASE_ID] as Long
        Log.e("EDIT_ACTIVITY","Extra : $databaseId")
        if (databaseId > 0){
            mViewModel.getEstatesByID(databaseId).observe(this, Observer { updateUI(it!!) })
        }
    }

    private fun showOverlay(context: Context, image: Image, position: Int){
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog_overlay)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(true)

        val displayMetrics = context.resources.displayMetrics
        val dialogWidth = (displayMetrics.widthPixels * 0.85).toInt()
        val dialogHeight = (displayMetrics.heightPixels * 0.85).toInt()
        dialog.window.setLayout(dialogWidth,dialogHeight)

        dialog.show()

        this.populateDialogWhenOpening(dialog,image)

        dialog.overlay_cancel.setOnClickListener{hideOverlayLayout(dialog)}
        dialog.overlay_save.setOnClickListener{}
    }

    private fun populateDialogWhenOpening(dialog: Dialog, image: Image){
        Glide.with(this).load(Uri.parse(image.imagePath)).apply(RequestOptions().centerCrop()).into(dialog.overlay_image_view)
        if(!(image.imageTitle.isNullOrBlank())) dialog.overlay_title.setText(image.imageTitle)
        if(!(image.imageDesc.isNullOrBlank())) dialog.overlay_desc.setText(image.imageDesc)
    }

    private fun hideOverlayLayout(dialog: Dialog){
        dialog.dismiss()
    }

    // ---------------------
    // ACTION
    // ---------------------

    override fun onClickDeleteButton(position: Int) {
        listImages.removeAt(position)
        adapter.notifyDataSetChanged()
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

        listImages.addAll(result.images)
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
}
