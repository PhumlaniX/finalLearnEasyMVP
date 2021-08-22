package com.finallearneasymvp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    //val ref = FirebaseAuth.getInstance()
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        register()

        /*tv_login.setOnClickListener {
            onBackPressed()
        }*/
    }

    private fun register() {

        btn_signup.setOnClickListener {

            if (TextUtils.isEmpty(usernameInput.text.toString())) {
                usernameInput.setError("Please enter first name")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(usersurnameInput.text.toString())) {
                usersurnameInput.setError("Please enter last name")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(et_signup_emailInput.text.toString())) {
                et_signup_emailInput.setError("Please enter valid email address")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(et_signup_passwordInput.text.toString())) {
                et_signup_passwordInput.setError("Please enter password")
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(
                et_signup_emailInput.text.toString(),
                et_signup_passwordInput.text.toString()
            )
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val currentUser = auth.currentUser
                        val currentUserDB = databaseReference?.child((currentUser?.uid!!))
                        currentUserDB?.child("firstname")
                            ?.setValue(usernameInput.text.toString())
                        currentUserDB?.child("lastname")
                            ?.setValue(usersurnameInput.text.toString())
                        currentUserDB?.child("Email")
                            ?.setValue(et_signup_emailInput.text.toString())
                        currentUserDB?.child("Password")
                            ?.setValue(et_signup_passwordInput.text.toString())
                        Toast.makeText(
                            this@RegisterActivity,
                            "Welcome!!! Successfully registered!!!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()

                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registration failed. Please retry registration",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            tv_login.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }

        }
    }
}


