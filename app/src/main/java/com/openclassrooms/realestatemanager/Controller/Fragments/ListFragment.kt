package com.openclassrooms.realestatemanager.Controller.Fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.openclassrooms.realestatemanager.Controller.ViewModel.EstateViewModel
import com.openclassrooms.realestatemanager.Controller.Views.FragmentListAdapter
import com.openclassrooms.realestatemanager.Di.Injection
import com.openclassrooms.realestatemanager.Models.Estate

import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * A simple [Fragment] subclass.
 *
 */
class ListFragment : Fragment() {

    private lateinit var listEstate:ArrayList<Estate>
    private lateinit var adapter:FragmentListAdapter
    private lateinit var mViewModel: EstateViewModel

    companion object {
        fun newInstance(): ListFragment {
            return ListFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("ListFragment", "Displaying fragment...")
        mViewModel = ViewModelProviders.of(this,Injection.provideViewModelFactory(this.context!!)).get(EstateViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        mViewModel.getEstates().observe(this, Observer<List<Estate>> { t -> updateUI(t!!) })

        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.configureRecyclerView()
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

    // ---------------------
    // UI
    // ---------------------

    private fun updateUI(results:List<Estate>){
        this.listEstate.clear()
        this.listEstate.addAll(results)
        adapter.notifyDataSetChanged()
    }

}
