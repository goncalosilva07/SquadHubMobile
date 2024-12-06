package com.example.squadhub.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.squadhub.R
import com.example.squadhub.model.User

class SquadAdapter(val players: ArrayList<User>):
    RecyclerView.Adapter<SquadAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardPlayer: CardView = itemView.findViewById(R.id.cardPlayer)
        val playerName: TextView = itemView.findViewById(R.id.playerName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_player, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = players[position]
        holder.playerName.text = p.name
        holder.cardPlayer.setOnClickListener {
            val context = holder.cardPlayer.context
            Toast.makeText(context, "pos: "+position, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return players.size
    }

}