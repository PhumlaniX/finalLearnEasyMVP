package com.finallearneasymvp

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    private lateinit var dialog : Dialog
    /*var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        //onStart()
        login()

        tv_register.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

    }

    /*public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        login(currentUser)
    }*/

    private fun login() {
        btn_login.setOnClickListener {

            if (TextUtils.isEmpty(emailInput.text.toString().trim { it <= ' '})) {
                emailInput.error = ("Please enter valid email address")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(passwordInput.text.toString().trim { it <= ' '})) {
                passwordInput.error = ("Please enter password")
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(emailInput.text.toString().trim { it <= ' '}, passwordInput.text.toString().trim { it <= ' '})
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        showProgressBar()
                        startActivity(Intent(this@LoginActivity, ProfileActivity::class.java))
                        finish()
                    } else {
                        hideProgressBar()
                        Toast.makeText(
                            this@LoginActivity,
                            "Login failed. Please retry login",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    private fun showProgressBar() {
        dialog = Dialog(this@LoginActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_wait)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }


    private fun hideProgressBar() {
        dialog.dismiss()
    }
}