package com.openclassrooms.realestatemanager.Controller.Activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import com.facebook.stetho.Stetho
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.openclassrooms.realestatemanager.Controller.Fragments.*
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {

    private var detailFragment: DetailFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Stetho.initializeWithDefaults(this)

        this.configureToolbar()
        this.configureBottomNavigationView()

        this.showFragment(ListFragment.newInstance())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.toolbar_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        when(id){
            R.id.menu_add -> launchActivity(this,AddActivity::class.java, null)
        }
        return super.onOptionsItemSelected(item)
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    private fun configureToolbar(){
        setSupportActionBar(simple_toolbar)
    }

    private fun configureBottomNavigationView(){
        bottom_navigation_view.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_menu_list -> showFragment(ListFragment.newInstance())
                R.id.nav_menu_map -> runWithPermissions(Constants.PERM_FINE_LOCATION,Constants.PERM_COARSE_LOCATION){showFragment(MapFragment.newInstance())}
                R.id.nav_menu_search -> showFragment(SearchFragment.newInstance())
                R.id.nav_menu_sim -> showFragment(LoanFragment.newInstance())
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun setBottomItemSelected(newFragment: Fragment){
        when(newFragment){
            ListFragment.newInstance() ->bottom_navigation_view.selectedItemId = (R.id.nav_menu_list)
            SearchFragment.newInstance() -> bottom_navigation_view.selectedItemId = (R.id.nav_menu_search)
            MapFragment.newInstance() -> bottom_navigation_view.selectedItemId = (R.id.nav_menu_map)
            LoanFragment.newInstance() -> bottom_navigation_view.selectedItemId = (R.id.nav_menu_sim)
        }

    }

    // ---------------------
    // ACTIVITY
    // ---------------------

    fun <T>launchActivity(context: Context, mClass:Class<T>, extra:Any?){
        val intent = Intent(context, mClass)
        if (extra != null){
            if (extra is Map<*,*>){
                intent.putExtra(DATABASE_ID,extra[DATABASE_ID] as Long)
            }
        }
        startActivity(intent)
    }

    // ---------------------
    // FRAGMENTS
    // ---------------------

    fun showFragment(newFragment: Fragment){
        //this.setBottomItemSelected(newFragment)

        val transaction = supportFragmentManager.beginTransaction()

        if(isTablet()){
            val previousFragment = supportFragmentManager.fragments
            if (previousFragment.isNotEmpty()){
                (0 until previousFragment.size).forEach{
                    if (previousFragment[it] !is ListFragment)
                        transaction.remove(previousFragment[it])
                }
            }

        }
        if(isTablet() && newFragment !is ListFragment){
            transaction.replace(R.id.fragment_view_detail, newFragment)
        }else{
            transaction.replace(R.id.fragment_view, newFragment)
        }

        transaction.commit()
    }

    fun isTablet() = detailFragment == null && findViewById<FrameLayout>(R.id.fragment_view_detail) != null
}
