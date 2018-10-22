package com.openclassrooms.realestatemanager.Controller.Views

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.openclassrooms.realestatemanager.Controller.ViewModel.EstateViewModel
import com.openclassrooms.realestatemanager.Models.Estate
import com.openclassrooms.realestatemanager.Models.FullEstate
import com.openclassrooms.realestatemanager.R

/**
 * Created by Adrien Deguffroy on 13/10/2018.
 */
class FragmentListAdapter(private val listEstate:List<FullEstate>) : RecyclerView.Adapter<FragmentListViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FragmentListViewHolder {
       return FragmentListViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.fragment_list_item,p0,false))
    }

    override fun getItemCount(): Int {
       return if (listEstate.isNotEmpty()) listEstate.size else 0
    }

    fun getEstateInfos(position:Int):FullEstate{
        return listEstate[position]
    }

    fun setData(){

    }

    override fun onBindViewHolder(p0: FragmentListViewHolder, p1: Int) {
        p0.updateWithData(this.listEstate[p1], p1)
    }
}