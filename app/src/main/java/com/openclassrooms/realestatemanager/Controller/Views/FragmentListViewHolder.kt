package com.openclassrooms.realestatemanager.Controller.Views

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.openclassrooms.realestatemanager.Controller.ViewModel.EstateViewModel
import com.openclassrooms.realestatemanager.Di.Injection
import com.openclassrooms.realestatemanager.Models.Estate
import com.openclassrooms.realestatemanager.Models.FullEstate
import com.openclassrooms.realestatemanager.Models.Image
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.fragment_list_item.view.*

/**
 * Created by Adrien Deguffroy on 13/10/2018.
 */
class FragmentListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun updateWithData(estate: FullEstate, position:Int, lastItemClick:Int){
        val glide: RequestManager = Glide.with(itemView)

       if (position == lastItemClick){
           itemView.setBackgroundColor(ContextCompat.getColor(itemView.context,R.color.colorAccentSelected))
       }else{
           itemView.setBackgroundColor(ContextCompat.getColor(itemView.context,R.color.colorWhite))
       }

        if (estate.images.isNotEmpty()){
            glide.load(Uri.parse(estate.images[0].imagePath)).apply(RequestOptions().centerCrop()).into(itemView.list_item_main_pic)
        }else{
            glide.load(R.drawable.ic_no_image_available).apply(RequestOptions().centerCrop()).into(itemView.list_item_main_pic)
        }

        if (estate.estate.estateType.isNullOrEmpty()){
            itemView.list_item_type.text = itemView.resources.getString(R.string.list_fragment_no_type)
        }else{
            itemView.list_item_type.text = estate.estate.estateType.toString()
        }

       if (estate.location.city.isNullOrEmpty()){
           itemView.list_item_city.text = itemView.resources.getString(R.string.list_fragment_no_city)
       }else{
           itemView.list_item_city.text = "${estate.location.city}"
       }

        if (estate.estate.price == null){
            itemView.list_item_price.text = itemView.resources.getString(R.string.list_fragment_no_price)
        }else{
            itemView.list_item_price.text = "${estate.estate.price}"
        }


    }
}