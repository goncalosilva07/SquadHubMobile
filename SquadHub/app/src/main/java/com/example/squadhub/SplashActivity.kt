package com.example.squadhub

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.squadhub.model.User

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({

            val login = getSharedPreferences("squadhubSP", Context.MODE_PRIVATE).getBoolean("login", false)
            val id = getSharedPreferences("squadhubSP", Context.MODE_PRIVATE).getInt("idUser", -1)

            if (login){
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
            }else{
                startActivity(Intent(this, LoginActivity::class.java))
            }
            finish()
        }, 3000)
    }
}