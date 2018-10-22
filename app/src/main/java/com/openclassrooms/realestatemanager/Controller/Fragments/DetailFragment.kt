package com.openclassrooms.realestatemanager.Controller.Fragments


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.openclassrooms.realestatemanager.Controller.Activities.EditActivity
import com.openclassrooms.realestatemanager.Controller.Activities.MainActivity
import com.openclassrooms.realestatemanager.Controller.ViewModel.EstateViewModel
import com.openclassrooms.realestatemanager.Controller.ViewModel.ViewModelFactory
import com.openclassrooms.realestatemanager.Di.Injection
import com.openclassrooms.realestatemanager.Models.Estate
import com.openclassrooms.realestatemanager.Models.FullEstate

import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 * A simple [Fragment] subclass.
 *
 */
class DetailFragment : Fragment() {

    private lateinit var mViewModel:EstateViewModel
    private var databaseId:Any? = null

    companion object {
        fun newInstance():DetailFragment{
            return DetailFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        mViewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this.context!!)).get(EstateViewModel::class.java)

        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.retrieveDatabaseId()
        this.configureFABOnClickListener()
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    private fun configureRecyclerView(){

    }

    private fun retrieveDatabaseId(){
        databaseId = arguments?.get(DATABASE_ID)
        Log.e("DETAIL_FRAGMENT","Id retrieved : $databaseId")
        if(databaseId != null){
            mViewModel.getEstatesByID(databaseId as Long).observe(this, Observer {
                updateUI(it!!)
            })
        }
    }

    private fun configureFABOnClickListener(){
        detail_fragment_fab.setOnClickListener {
            if (databaseId != null){
                val map = hashMapOf(DATABASE_ID to databaseId as Long)
                (activity as MainActivity).launchActivity(((activity as MainActivity).applicationContext),EditActivity::class.java,map)
            }
        }
    }

    // ---------------------
    // UI
    // ---------------------

    private fun updateUI(result:FullEstate){
        detail_fragment_edit_text.setText(result.estate.desc)
        detail_fragment_surface.text = result.estate.surface.toString()
        detail_fragment_rooms.text = result.estate.roomNumber.toString()
        detail_fragment_bathrooms.text = result.estate.bathroomNumber.toString()
        detail_fragment_bedrooms.text = result.estate.bedroomNumber.toString()
    }
}
