package com.example.squadhub.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.squadhub.LoginActivity
import com.example.squadhub.MainActivity
import com.example.squadhub.R

class PerfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.logout).setOnClickListener {
            logout(view)
        }
    }

    fun logout(view: View){
        view.context.getSharedPreferences("squadhubSP", Context.MODE_PRIVATE).edit().putBoolean("login", false).putInt("idUser", -1).apply()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        //requireContext().getSharedPreferences("squadhubSP", Context.MODE_PRIVATE).edit().putBoolean("login", false).putInt("idUser", -1).apply()
    }
}