package com.finallearneasymvp

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
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
    private lateinit var dialog : Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("profile")

        register()

        tv_login.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

        /*tv_login.setOnClickListener {
            onBackPressed()
        }*/
    }

    private fun register() {

        btn_signup.setOnClickListener {

            if (TextUtils.isEmpty(usernameInput.text.toString().trim{it <= ' '})) {
                usernameInput.setError("Please enter first name")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(usersurnameInput.text.toString().trim{it <= ' '})) {
                usersurnameInput.setError("Please enter last name")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(et_signup_emailInput.text.toString().trim{it <= ' '})) {
                et_signup_emailInput.setError("Please enter valid email address")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(et_signup_passwordInput.text.toString().trim{it <= ' '})) {
                et_signup_passwordInput.setError("Please enter password")
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(
                et_signup_emailInput.text.toString().trim { it <= ' '},
                et_signup_passwordInput.text.toString().trim { it <= ' '}
            )
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val currentUser = auth.currentUser
                        val currentUserDB = databaseReference?.child((currentUser?.uid!!))
                        currentUserDB?.child("firstname")
                            ?.setValue(usernameInput.text.toString().trim { it <= ' '})
                        currentUserDB?.child("lastname")
                            ?.setValue(usersurnameInput.text.toString().trim { it <= ' '})
                        currentUserDB?.child("Email")
                            ?.setValue(et_signup_emailInput.text.toString().trim { it <= ' '})
                        currentUserDB?.child("Password")
                            ?.setValue(et_signup_passwordInput.text.toString().trim { it <= ' '})

                        /*val user = User (
                            currentUserDB.uid,
                            usernameInput.text.toString().trim{it <= ' '},
                            usersurnameInput.text.toString().trim{it <= ' '},
                            usersurnameInput.text.toString().trim{it <= ' '},
                            et_signup_passwordInput.text.toString().trim{it <= ' '}
                        )*/
                        showProgressBar()
                        startActivity(Intent(this@RegisterActivity, ProfileActivity::class.java))
                        Toast.makeText(
                            this@RegisterActivity,
                            "Welcome!!! Successfully registered!!!",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()

                    } else {
                        hideProgressBar()
                        Toast.makeText(
                            this@RegisterActivity,
                            "Registration failed. Please retry registration",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun showProgressBar() {
        dialog = Dialog(this@RegisterActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_wait)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }


    private fun hideProgressBar() {
        dialog.dismiss()
    }
}


