package com.openclassrooms.realestatemanager.Controller.Fragments

import android.app.DatePickerDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import com.openclassrooms.realestatemanager.Controller.Activities.MainActivity
import com.openclassrooms.realestatemanager.Controller.ViewModel.EstateViewModel
import com.openclassrooms.realestatemanager.Di.Injection
import com.openclassrooms.realestatemanager.Models.FullEstate

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.Constants
import com.openclassrooms.realestatemanager.Utils.Utils
import com.openclassrooms.realestatemanager.Utils.toFRDate
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 *
 */
class SearchFragment : Fragment() {

    private lateinit var mViewModel: EstateViewModel
    private var sectorList = listOf<String>()

    companion object {
        fun newInstance(): SearchFragment{
            return SearchFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        mViewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this.context!!)).get(EstateViewModel::class.java)
        mViewModel.getSectorList()

        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel.listSector.observe(this, Observer { sectorList = mViewModel.getListSector() })

        this.setOnClickListener()
        this.populateToDate()
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    private fun populateToDate(){
        search_fragment_to_date.setText(Utils.getTodayDate(),TextView.BufferType.EDITABLE)
    }

    private fun setOnClickListener(){
        search_fragment_sector.setOnClickListener { this.displayPopupMenu(sectorList.toTypedArray(), search_fragment_sector) }
        search_fragment_spinner.setOnClickListener{ this.displayPopupMenu(resources.getStringArray(R.array.search_type_array), search_fragment_spinner) }
        search_fragment_statute.setOnClickListener{ this.displayPopupMenu(resources.getStringArray(R.array.search_statute_array), search_fragment_statute)}
        search_fragment_from_date.setOnClickListener { this.displayDatePicker(search_fragment_from_date) }
        search_fragment_to_date.setOnClickListener { this.displayDatePicker(search_fragment_to_date) }
        search_fragment_FAB.setOnClickListener { this.buildSearchQuery() }
    }

    private fun displayPopupMenu(listToDisplay:Array<String>,view : TextInputEditText){
        val popupMenu = PopupMenu(this.context,view)
        (0 until listToDisplay.size).forEach{
            popupMenu.menu.add(Menu.NONE,it,it,listToDisplay[it])
            popupMenu.setOnMenuItemClickListener { view.setText(it.title);true }
        }
        popupMenu.show()
    }

    private fun displayDatePicker(mView:TextInputEditText){
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val calendar: Calendar = Calendar.getInstance()
        val mYear = calendar.get(Calendar.YEAR)
        val mMonth = calendar.get(Calendar.MONTH)
        val mDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this.context, DatePickerDialog.OnDateSetListener{ view, year, monthOfYear, dayOfMonth ->
            mView.setText(sdf.format(Date(year - 1900,monthOfYear,dayOfMonth)), TextView.BufferType.EDITABLE)

        }, mYear,mMonth,mDay)
        datePickerDialog.show()
    }

    // ---------------------
    // SQL QUERY
    // ---------------------

    private fun buildSearchQuery(){
        val estateType = search_fragment_spinner.text.toString()
        val estateStatute = search_fragment_statute.text.toString()
        val estatePriceMin = search_fragment_price_min.text.toString().toDoubleOrNull()
        val estatePriceMax = search_fragment_price_max.text.toString().toDoubleOrNull()
        val estateSurfaceMin = search_fragment_surface_min.text.toString().toIntOrNull()
        val estateSurfaceMax = search_fragment_surface_max.text.toString().toIntOrNull()
        val estateBedroomMin = search_fragment_bedrooms_min.text.toString().toIntOrNull()
        val estateBedroomMax = search_fragment_bedrooms_max.text.toString().toIntOrNull()
        val estateSector = search_fragment_sector.text.toString()
        val estatePark = search_fragment_nearby_parks.isChecked ; val estateShop = search_fragment_nearby_shops.isChecked
        val estateSchool = search_fragment_nearby_schools.isChecked ; val estateHighway = search_fragment_nearby_highway.isChecked
        val estateFromDate = try { search_fragment_from_date.text.toString().toFRDate() }catch (e:Exception){ null }
        val estateToDate = try { search_fragment_to_date.text.toString().toFRDate() }catch (e:Exception){ null }
        val estatePhoto = search_fragment_media_min.text.toString().toIntOrNull()?: 0

        var query = "SELECT DISTINCT * FROM Estate,(SELECT * FROM Image,Estate WHERE Image.estateId = Estate.id) AS images INNER JOIN Location ON Estate.id = Location.estateId"
        val args = arrayListOf<Any>()
        var conditions = false

        if (!(estateType == "" || estateType == "ALL")){
            query += " WHERE estateType = :$estateType"
            args.add(estateType)
            conditions = true
        }

        if (!(estateStatute == "" || estateStatute == "ALL")){
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "estateStatute = :$estateStatute"
            args.add(estateStatute)
        }

        if (estatePriceMin != null){
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "price >= :${estatePriceMin.toInt()}"
            args.add(estatePriceMin.toInt())
        }

        if (estatePriceMax != null){
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "price <= :${estatePriceMax.toInt()}"
            args.add(estatePriceMax.toInt())
        }

        if (estateSurfaceMin != null){
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "surface >= :$estateSurfaceMin"
            args.add(estateSurfaceMin)
        }

        if (estateSurfaceMax != null){
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "surface <= :$estateSurfaceMax"
            args.add(estateSurfaceMax)
        }

        if (estateBedroomMin != null){
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "bedroomNumber >= :$estateBedroomMin"
            args.add(estateBedroomMin)
        }

        if (estateBedroomMax != null){
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "bedroomNumber <= :$estateBedroomMax"
            args.add(estateBedroomMax)
        }

        if (estateSector != ""){
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "sector = ?"
            args.add(estateSector)
        }

        if (estatePark){
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "parks = :$estatePark"
            args.add(estatePark)
        }

        if (estateShop){
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "shops = :$estateShop"
            args.add(estateShop)
        }

        if (estateSchool){
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "schools = :$estateSchool"
            args.add(estateSchool)
        }

        if (estateHighway){
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "highway = :$estateHighway"
            args.add(estateHighway)
        }

        if (estateFromDate != null){
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "entryDate >= ?"
            args.add(estateFromDate.time)
        }

       /* if (estateToDate != null){
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "entryDate <= ?"
            args.add(estateToDate.time)
        }*/

        if (estatePhoto != null) {
            //query += if (conditions) " AND " else " WHERE "; conditions = true
            query += " GROUP BY Estate.id"
            //args.add(estatePhoto)
        }

        this.launchListFragment(query,args)

        mViewModel.getEstatesBySearch(query,args).observe(this, Observer {
            describeResult(it!!)
        })
    }

    private fun describeResult(result:List<FullEstate>){
        (0 until result.size).forEach {
            Log.e("RESULT_FROM_SEARCH","RESULT : ${result[it].estate}")
        }
    }

    // ---------------------
    // ACTION
    // ---------------------

    private fun launchListFragment(query:String,args:ArrayList<Any>){
        val bundle = Bundle()
        val newFragment = ListFragment.newInstance()
        bundle.putString("QUERY",query)
        bundle.putStringArrayList("ARGS", args as java.util.ArrayList<String>)
        newFragment.arguments = bundle

        (activity as MainActivity).showFragment(newFragment)
    }
}
