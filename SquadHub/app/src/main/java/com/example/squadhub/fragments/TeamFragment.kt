package com.example.squadhub.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.squadhub.GamesActivity
import com.example.squadhub.MainActivity
import com.example.squadhub.R
import com.example.squadhub.SquadActivity
import com.example.squadhub.TrainingSessionActivity
import com.example.squadhub.model.TrainingSession

class TeamFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireView().findViewById<Button>(R.id.squadBtn).setOnClickListener {
            val intent = Intent(requireContext(), SquadActivity::class.java)
            startActivity(intent)
        }

        requireView().findViewById<Button>(R.id.gamesBtn).setOnClickListener {
            val intent = Intent(requireContext(), GamesActivity::class.java)
            startActivity(intent)
        }

        requireView().findViewById<Button>(R.id.trainingBtn).setOnClickListener {
            val intent = Intent(requireContext(), TrainingSessionActivity::class.java)
            startActivity(intent)
        }
    }

}