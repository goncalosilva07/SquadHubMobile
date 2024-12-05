package com.example.squadhub

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class UserTypeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_type)


    }

    fun chooseRole(role: Int){
        val intent = Intent(this, RegisterActivity::class.java)
        intent.putExtra("role", role)
        startActivity(intent)
    }

    fun admin(view: View){
        val role = 1
        chooseRole(role)
    }

    fun coach(view: View){
        val role = 2
        chooseRole(role)
    }

    fun player(view: View){
        val role = 3
        chooseRole(role)
    }

    fun staff(view: View){
        val role = 4
        chooseRole(role)
    }
}