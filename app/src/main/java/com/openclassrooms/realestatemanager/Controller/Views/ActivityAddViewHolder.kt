package com.openclassrooms.realestatemanager.Controller.Views

import android.graphics.Color
import android.graphics.LightingColorFilter
import android.graphics.drawable.Drawable
import android.net.Uri
import android.opengl.Visibility
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.openclassrooms.realestatemanager.Controller.Activities.VIEWHOLDER_ACTION_ADD
import com.openclassrooms.realestatemanager.Controller.Activities.VIEWHOLDER_ACTION_DETAIL
import com.openclassrooms.realestatemanager.Controller.Activities.VIEWHOLDER_ACTION_EDIT
import com.openclassrooms.realestatemanager.Controller.Fragments.DetailFragment
import com.openclassrooms.realestatemanager.Models.Image
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.activity_add_item.view.*
import java.lang.ref.WeakReference

/**
 * Created by Adrien Deguffroy on 10/10/2018.
 */
class ActivityAddViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var callbackWeakRef:WeakReference<ActivityAddAdapter.Listener>


    fun updateWithData(image:Image, callback:ActivityAddAdapter.Listener, action:String){
        val glide:RequestManager = Glide.with(itemView)
        lateinit var icon:Drawable
        lateinit var filter:LightingColorFilter

        glide.load(Uri.parse(image.imagePath)).apply(RequestOptions().centerCrop()).into(itemView.horizontal_item_view_image)

        if (action == VIEWHOLDER_ACTION_ADD || action == VIEWHOLDER_ACTION_EDIT){
            itemView.horizontal_image_title.visibility = View.GONE
            itemView.horizontal_item_statute.visibility = View.VISIBLE
            itemView.horizontal_item_delete.visibility = View.VISIBLE
            if (image.imageTitle != null && image.imageTitle!!.isNotEmpty() && image.imageDesc != null && image.imageDesc!!.isNotEmpty()){
                icon = itemView.resources.getDrawable(R.drawable.baseline_check_circle_black_24)
                filter = LightingColorFilter(Color.GREEN, Color.GREEN)
            }else{
                icon = itemView.resources.getDrawable(R.drawable.baseline_warning_white_24)
                filter = LightingColorFilter(Color.RED, Color.RED)
            }
            icon.colorFilter = filter
            itemView.horizontal_item_statute.background = icon
        }else if(action == VIEWHOLDER_ACTION_DETAIL){
            itemView.horizontal_image_title.visibility = View.VISIBLE
            itemView.horizontal_item_statute.visibility = View.GONE
            itemView.horizontal_item_delete.visibility = View.GONE

            itemView.horizontal_image_title.text = image.imageTitle
        }

        itemView.horizontal_item_delete.setOnClickListener{this.onClick()}
        this.callbackWeakRef = WeakReference(callback)
    }

    fun onClick(){
        val callback = callbackWeakRef.get()
        callback!!.onClickDeleteButton(adapterPosition)
    }
}