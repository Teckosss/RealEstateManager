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
import com.openclassrooms.realestatemanager.Controller.Fragments.DATABASE_ID
import com.openclassrooms.realestatemanager.Controller.Fragments.DetailFragment
import com.openclassrooms.realestatemanager.Controller.Fragments.ListFragment
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.toolbar.*

const val VIEWHOLDER_ACTION_ADD = "ADD"
const val VIEWHOLDER_ACTION_EDIT = "EDIT"
const val VIEWHOLDER_ACTION_DETAIL = "DETAIL"

class MainActivity : AppCompatActivity() {

    private var detailFragment: DetailFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Stetho.initializeWithDefaults(this)

        this.configureToolbar()

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
            R.id.menu_edit -> launchActivity(this,EditActivity::class.java,null)
            R.id.menu_search -> launchActivity(this,SearchActivity::class.java,null)
        }
        return super.onOptionsItemSelected(item)
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    private fun configureToolbar(){
        setSupportActionBar(simple_toolbar)
    }

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

    private fun showFragment(newFragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()

        if (isTablet()){
            detailFragment = DetailFragment.newInstance()
            transaction.add(R.id.fragment_view_detail, detailFragment as DetailFragment)
        }

        transaction.replace(R.id.fragment_view, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun isTablet() = detailFragment == null && findViewById<FrameLayout>(R.id.fragment_view_detail) != null
}
