package com.example.squadhub

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isEmpty
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.squadhub.model.Permissions.Companion.parsePermissionsManually
import com.example.squadhub.databinding.ActivityMainBinding
import com.example.squadhub.fragments.HomeFragment
import com.example.squadhub.fragments.PerfilFragment
import com.example.squadhub.fragments.TeamFragment
import com.example.squadhub.fragments.UserWithoutClubFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    companion object {
        lateinit var navigation: BottomNavigationView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Config(applicationContext)
        navigation = binding.navigation

        manageMenuItems(navigation.menu)

        binding.navigation.setOnItemSelectedListener {
            binding.placeholder.removeAllViews()
            when(it.itemId) {
                R.id.nav_1 -> {
                    binding.menuTitle.text = "Home"
                    supportFragmentManager
                        .beginTransaction()
                        .add(R.id.placeholder, HomeFragment() )
                        .commit() }
                R.id.nav_2 -> {
                    binding.menuTitle.text = "Team"
                    supportFragmentManager
                        .beginTransaction()
                        .add(R.id.placeholder, TeamFragment() )
                        .commit() }
                R.id.nav_3 -> {
                    binding.menuTitle.text = "Notifications"
                    supportFragmentManager
                        .beginTransaction()
                        .add(R.id.placeholder, HomeFragment() )
                        .commit() }
                R.id.nav_4 -> {
                    binding.menuTitle.text = "Perfil"
                    supportFragmentManager
                        .beginTransaction()
                        .add(R.id.placeholder, PerfilFragment() )
                        .commit() }
                R.id.nav_5 -> {
                    binding.menuTitle.text = "Home"
                    supportFragmentManager
                        .beginTransaction()
                        .add(R.id.placeholder, UserWithoutClubFragment() )
                        .commit() }
            }
            return@setOnItemSelectedListener true
        }

    }

    override fun onStart() {
        super.onStart()
        if(binding.placeholder.isEmpty() && Config.club != null) {
            binding.navigation.selectedItemId = R.id.nav_1
        }else if (binding.placeholder.isEmpty() && Config.club == null){
            binding.navigation.selectedItemId = R.id.nav_5
        }
    }

    private fun manageMenuItems(menu: Menu) {
        // Se idClub estiver vazio, ocultar alguns itens

        if (Config.club == null){
            menu.findItem(R.id.nav_1).isVisible = false
            menu.findItem(R.id.nav_2).isVisible = false
        }else{
            menu.findItem(R.id.nav_5).isVisible = false
        }
    }

}