package com.openclassrooms.realestatemanager.Controller.Activities

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.facebook.stetho.Stetho
import com.google.firebase.analytics.FirebaseAnalytics
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.openclassrooms.realestatemanager.Controller.Fragments.*
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils.Constants
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {

    private var detailFragment: DetailFragment? = null
    private lateinit var mFirebaseAnalytics: FirebaseAnalytics
    private lateinit var mFragmentTag:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Stetho.initializeWithDefaults(this)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        this.configureToolbar()
        this.configureBottomNavigationView()

        if (savedInstanceState != null){
            val tag = savedInstanceState.getString(Constants.FRAGMENT_TAG_KEY) ?: null
            if (tag != null){
                when(tag){
                   Constants.FRAGMENT_LIST -> showFragment(ListFragment.newInstance())
                   Constants.FRAGMENT_SEARCH -> showFragment(SearchFragment.newInstance())
                   Constants.FRAGMENT_MAP -> showFragment(MapFragment.newInstance())
                   Constants.FRAGMENT_LOAN -> showFragment(LoanFragment.newInstance())
                }
            }
        }else{
            this.showFragment(ListFragment.newInstance())
        }
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

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString(Constants.FRAGMENT_TAG_KEY, mFragmentTag)
        super.onSaveInstanceState(outState)
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
                R.id.nav_menu_map -> runWithPermissions(Constants.PERM_FINE_LOCATION,Constants.PERM_COARSE_LOCATION){ showFragment(MapFragment.newInstance()) }
                R.id.nav_menu_search -> showFragment(SearchFragment.newInstance())
                R.id.nav_menu_loan -> showFragment(LoanFragment.newInstance())
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    private fun setBottomItemSelected(newFragment: Fragment){
        when(newFragment){
            ListFragment.newInstance() -> bottom_navigation_view.selectedItemId = (R.id.nav_menu_list)
            SearchFragment.newInstance() -> bottom_navigation_view.selectedItemId = (R.id.nav_menu_search)
            MapFragment.newInstance() -> bottom_navigation_view.selectedItemId = (R.id.nav_menu_map)
            LoanFragment.newInstance() -> bottom_navigation_view.selectedItemId = (R.id.nav_menu_loan)
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

        //if (isTablet()) this.changeLayout(resources.configuration.orientation)

        when(newFragment){
            is ListFragment -> this.mFragmentTag = Constants.FRAGMENT_LIST
            is SearchFragment -> this.mFragmentTag = Constants.FRAGMENT_SEARCH
            is MapFragment -> this.mFragmentTag = Constants.FRAGMENT_MAP
            is LoanFragment -> this.mFragmentTag = Constants.FRAGMENT_LOAN

        }

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

    private fun changeLayout(orientation:Int){
        fragment_view_container.requestLayout()
        fragment_view_detail_container.requestLayout()

        val fragmentLayoutParams = fragment_view_container.layoutParams
        val fragmentDetailLayoutParams = fragment_view_detail_container.layoutParams

        val constraintLayout = findViewById<ConstraintLayout>(R.id.fragment_view_container)
        val set = ConstraintSet()
        set.clone(constraintLayout)
        set.clear(R.id.fragment_view_container)


        val constraintLayoutDetail = findViewById<ConstraintLayout>(R.id.fragment_view_detail_container)
        val setDetail = ConstraintSet()
        setDetail.clone(constraintLayoutDetail)
        setDetail.clear(R.id.fragment_view_detail_container)


        if (orientation == Configuration.ORIENTATION_PORTRAIT){
            set.constrainPercentHeight(R.id.fragment_view_container,0.4F)

            set.applyTo(constraintLayout)

            fragmentLayoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT
            fragmentLayoutParams.height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
            fragment_view_container.layoutParams = fragmentLayoutParams

            setDetail.constrainPercentHeight(R.id.fragment_view_detail_container, 0.6F)
            setDetail.connect(R.id.fragment_view_detail_container, ConstraintSet.TOP, R.id.fragment_view_container,ConstraintSet.BOTTOM)
            setDetail.connect(R.id.fragment_view_detail_container, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM)
            setDetail.applyTo(constraintLayoutDetail)

            fragmentDetailLayoutParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT
            fragmentDetailLayoutParams.height = ConstraintLayout.LayoutParams.MATCH_CONSTRAINT
            fragment_view_detail_container.layoutParams = fragmentDetailLayoutParams



        }else{

        }
    }
}
