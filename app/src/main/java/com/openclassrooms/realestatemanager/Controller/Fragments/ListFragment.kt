package com.openclassrooms.realestatemanager.Controller.Fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.openclassrooms.realestatemanager.Controller.Activities.MainActivity
import com.openclassrooms.realestatemanager.Controller.Activities.TABLET_MODE
import com.openclassrooms.realestatemanager.Controller.ViewModel.EstateViewModel
import com.openclassrooms.realestatemanager.Controller.Views.FragmentListAdapter
import com.openclassrooms.realestatemanager.Di.Injection
import com.openclassrooms.realestatemanager.Models.Estate

import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.ItemClickSupport
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * A simple [Fragment] subclass.
 *
 */

const val DATABASE_ID = "DATABASE_ID"

class ListFragment : Fragment() {

    private lateinit var listEstate:ArrayList<Estate>
    private lateinit var adapter:FragmentListAdapter
    private lateinit var mViewModel: EstateViewModel

    companion object {
        fun newInstance(): ListFragment {
            return ListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        Log.e("ListFragment", "Displaying fragment...")
        mViewModel = ViewModelProviders.of(this,Injection.provideViewModelFactory(this.context!!)).get(EstateViewModel::class.java)

        mViewModel.getEstates().observe(this, Observer<List<Estate>> { t -> updateUI(t!!) })

        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.configureRecyclerView()
        this.configureOnClickRecyclerView()
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    private fun configureRecyclerView(){
        this.listEstate = ArrayList()
        this.adapter = FragmentListAdapter(this.listEstate)
        this.fragment_list_recycler_view.adapter = this.adapter
        this.fragment_list_recycler_view.layoutManager = LinearLayoutManager(activity)
    }

    private fun configureOnClickRecyclerView(){
        ItemClickSupport.addTo(fragment_list_recycler_view, R.layout.fragment_list_item)
                .setOnItemClickListener{recyclerView, position, v ->
                    this.launchDetailFragment(adapter.getEstateInfos(position).id)
        }
    }

    // ---------------------
    // ACTION
    // ---------------------

    private fun launchDetailFragment(databaseId : Long){
        val bundle = Bundle()
        val newFragment = DetailFragment.newInstance()
        bundle.putLong(DATABASE_ID,databaseId)
        newFragment.arguments = bundle

        val transaction = activity!!.supportFragmentManager.beginTransaction()

        if(TABLET_MODE){
            transaction.replace(R.id.fragment_view_detail, newFragment)
        }else{
            transaction.replace(R.id.fragment_view, newFragment)
        }

        transaction.addToBackStack(null)
        transaction.commit()

    }

    // ---------------------
    // UI
    // ---------------------

    private fun updateUI(results:List<Estate>){
        this.listEstate.clear()
        this.listEstate.addAll(results)
        adapter.notifyDataSetChanged()
    }

}
