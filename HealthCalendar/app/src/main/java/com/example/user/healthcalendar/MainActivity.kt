package com.example.user.healthcalendar

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.user.healthcalendar.Fragments.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val fCalendar = FragmentCalendar()
    private val fDoctors = FragmentDoctors()
    private val fMeds = FragmentMeds()
    private val fExam = FragmentExam()
    private val fJournal = FragmentJournal()
    private val fManage = FragmentManage()
    private val fHome = FragmentHome()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        supportFragmentManager.beginTransaction().replace(R.id.container, fHome).commit()
        
        nav_view.setNavigationItemSelectedListener(this)

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        val fTrans = supportFragmentManager.beginTransaction()

        when (item.itemId) {
            R.id.nav_home -> {
                fTrans.replace(R.id.container, fHome)
            }
            R.id.nav_calendar -> {
                fTrans.replace(R.id.container, fCalendar)
            }
            R.id.nav_doctors -> {
                fTrans.replace(R.id.container, fDoctors)
            }
            R.id.nav_meds -> {
                fTrans.replace(R.id.container, fMeds)
            }
            R.id.nav_exam -> {
                fTrans.replace(R.id.container, fExam)
            }
            R.id.nav_journal -> {
                fTrans.replace(R.id.container, fJournal)
            }
            R.id.nav_manage -> {
                fTrans.replace(R.id.container, fManage)
            }
        }
        fTrans.commit()

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
