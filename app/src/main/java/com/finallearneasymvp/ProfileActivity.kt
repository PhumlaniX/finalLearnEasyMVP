package com.finallearneasymvp

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.finallearneasymvp.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_register.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri : Uri
    private lateinit var dialog : Dialog
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val userId = intent.getStringExtra("user_id")
        //val emailId = intent.getStringExtra("email_id")

        //tv_user_id.text = "User ID :: $userId"
        //tv_email_id.text = "Email ID :: $emailId"

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("Profile")
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val uid = auth.currentUser?.uid

        loadProfile()

        binding.btnUpdate.setOnClickListener {
            showProgressBar()
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val bio = binding.etBio.text.toString()

            val user = User(firstName, lastName, bio)

            if (uid != null) {
                databaseReference.child(uid).setValue(user).addOnCompleteListener {
                    if (it.isSuccessful){
                        uploadProfilePic()
                    } else {
                        Toast.makeText(this@ProfileActivity, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            tv_login.setOnClickListener {
                startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
            }

            btnlogout.setOnClickListener {
                auth.signOut()
                startActivity(Intent(this@ProfileActivity, WelcomeActivity::class.java))
                finish()
            }

            /*FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
            finish()*/
        }

    }

    private fun loadProfile() {
        val user = auth.currentUser
        val userreference = databaseReference?.child(user?.uid!!)

        tv_email_id.text = "Email: "+user?.email

        userreference?.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                tv_first_name.text = "First Name: "+snapshot.child("firstname").value.toString()
                tv_last_name.text = "Last Name:"+snapshot.child("lastname").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProfileActivity, "Failed to update first name and last name", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun uploadProfilePic() {
        imageUri = Uri.parse("android.resource://$packageName/${R.drawable.ic_account_circle}")
        storageReference = FirebaseStorage.getInstance().getReference("Users/"+auth.currentUser?.uid)
        storageReference.putFile(imageUri).addOnSuccessListener {
            hideProgressBar()
            Toast.makeText(this@ProfileActivity, "Profile picture successfully uploaded", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            hideProgressBar()
            Toast.makeText(this@ProfileActivity, "Failed to upload profile picture", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showProgressBar() {
        dialog = Dialog(this@ProfileActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_wait)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }


    private fun hideProgressBar() {
        dialog.dismiss()
    }
}