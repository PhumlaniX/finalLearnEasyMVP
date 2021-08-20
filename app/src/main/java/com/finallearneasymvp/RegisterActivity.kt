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
    private var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Profile")

        register()

        tv_login.setOnClickListener {
            onBackPressed()
        }

        tv_login.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

        /*val email = findViewById<EditText>(R.id.et_register_email)
        val password = findViewById<EditText>(R.id.et_register_password)
        val regButton = findViewById<Button>(R.id.btn_reg)

        regButton.setOnClickListener {
            ref.createUserWithEmailAndPassword(
                email.text.toString().trim(),
                password.text.toString().trim()
            )
        }*/


        /*btn_reg.setOnClickListener {
            when {
                TextUtils.isEmpty(
                    et_register_email.text.toString().toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter email",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(
                    et_register_password.text.toString().toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Please enter password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val email: String = et_register_email.text.toString().trim { it <= ' ' }
                    val password: String = et_register_password.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(
                            OnCompleteListener<AuthResult> { task ->
                                if (task.isSuccessful) {
                                    val firebaseUser: FirebaseUser = task.result!!.user!!

                                    Toast.makeText(
                                        this@RegisterActivity,
                                        "You have successfully registered",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    val intent =
                                        Intent(this@RegisterActivity, MainActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("user_id", firebaseUser.uid)
                                    intent.putExtra("email_id", email)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@RegisterActivity,
                                        task.exception!!.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                }
            }
        }*/
    }

    private fun register() {
        btn_signup.setOnClickListener {

            //var fName = findViewById<EditText>(R.id.et_first_nameInput)

            /*if(TextUtils.isEmpty(et_first_nameInput.getText().toString())) {
                et_first_nameInput.setError("Please enter your first name")
                return@setOnClickListener
            }

            else if(TextUtils.isEmpty(et_last_nameInput.getText().toString())) {
                et_first_nameInput.setError("Please enter your last name")
                return@setOnClickListener
            }*/

            if (TextUtils.isEmpty(et_signup_emailInput.getText().toString())) {
                et_signup_emailInput.setError("Please enter valid email address")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(et_signup_passwordInput.getText().toString())) {
                et_signup_passwordInput.setError("Please enter password")
                return@setOnClickListener
            }

            /*else if (TextUtils.isEmpty(et_signup_bio.getText().toString())) {
                et_signup_bio.setError("Please tell us more about yourself")
                return@setOnClickListener*/
        }

        auth.createUserWithEmailAndPassword(
            et_signup_emailInput.getText().toString(),
            passwordInput.getText().toString()
        )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val currentUser = auth.currentUser
                    val currentUserDB = databaseReference?.child((currentUser?.uid!!))
                    /*currentUserDB?.child("First Name")?.setValue(et_first_nameInput.getText().toString())
                        currentUserDB?.child("Last Name")?.setValue(et_last_nameInput.getText().toString())*/
                    currentUserDB?.child("Email")
                        ?.setValue(et_signup_emailInput.getText().toString())
                    currentUserDB?.child("Password")
                        ?.setValue(et_signup_passwordInput.getText().toString())
                    //currentUserDB?.child("Password")?.setValue(et_signup_bio.getText().toString())

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
    }
}


