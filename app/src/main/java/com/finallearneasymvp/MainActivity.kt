package com.finallearneasymvp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.finallearneasymvp.fragments.DashboardFragment
import com.finallearneasymvp.fragments.ExitFragment
import com.finallearneasymvp.fragments.InfoFragment
import com.finallearneasymvp.fragments.SettingsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    //Bottom Navigation Fragments
    private val dashboardFragment = DashboardFragment()
    private val settingsFragment = SettingsFragment()
    private val infoFragment = InfoFragment()
    private  val exitFragment = ExitFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(dashboardFragment)

        bottom_nav.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.ic_dashboard -> replaceFragment(dashboardFragment)
                R.id.ic_settings -> replaceFragment(settingsFragment)
                R.id.ic_info -> replaceFragment(infoFragment)
                R.id.ic_close -> replaceFragment(exitFragment)
            }
            true
        }

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        loadProfile()

        /*btn_logout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
            finish()
        }*/

    }


    private fun loadProfile() {
        val user = auth.currentUser
        val userreference = databaseReference?.child(user?.uid!!)

        userreference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tv_updatedfirst_name.text = "Hello "+snapshot.child("First Name").value.toString()
                tv_updatedlast_name.text = snapshot.child("Last Name").value.toString()
                tv_updatedbio.text = snapshot.child("Bio").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to get user details", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun replaceFragment(fragment: Fragment) {

        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }

    }
}