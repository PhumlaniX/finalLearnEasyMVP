package com.finallearneasymvp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        //FirebaseApp.initializeApp()

        val login = findViewById<Button>(R.id.btn_login)
        val reg = findViewById<Button>(R.id.btn_reg)

        login.setOnClickListener {
            Toast.makeText(this, "Welcome Back!!!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        reg.setOnClickListener {
            Toast.makeText(this, "Welcome!!! Register now!!!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    /*@Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }*/
}