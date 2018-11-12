package com.openclassrooms.realestatemanager.Controller.Views

import android.graphics.Color
import android.graphics.LightingColorFilter
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.openclassrooms.realestatemanager.Models.Image
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.Constants
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
        val colorGreen = ContextCompat.getColor(itemView.context, R.color.colorGreen)
        val colorRed = ContextCompat.getColor(itemView.context, R.color.colorRed)

        glide.load(Uri.parse(image.imagePath)).apply(RequestOptions().centerCrop()).into(itemView.horizontal_item_view_image)

        if (action == Constants.VIEW_HOLDER_ACTION_ADD || action == Constants.VIEW_HOLDER_ACTION_EDIT){
            itemView.horizontal_image_title.visibility = View.GONE
            itemView.horizontal_item_statute.visibility = View.VISIBLE
            itemView.horizontal_item_delete.visibility = View.VISIBLE
            if (image.imageTitle != null && image.imageTitle!!.isNotEmpty() && image.imageDesc != null && image.imageDesc!!.isNotEmpty()){
                icon = itemView.resources.getDrawable(R.drawable.baseline_check_circle_black_24)
                filter = LightingColorFilter(colorGreen, colorGreen)
            }else{
                icon = itemView.resources.getDrawable(R.drawable.baseline_warning_white_24)
                filter = LightingColorFilter(colorRed, colorRed)
            }
            icon.colorFilter = filter
            itemView.horizontal_item_statute.background = icon

        }else if(action == Constants.VIEW_HOLDER_ACTION_DETAIL){
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