package com.example.squadhub.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.squadhub.Config
import com.example.squadhub.R
import com.example.squadhub.model.Game

class GamesAdapter(val games: ArrayList<Game>): RecyclerView.Adapter<GamesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardGame: CardView = itemView.findViewById(R.id.cardGame)
        val clubTextView: TextView = itemView.findViewById(R.id.clubTextView)
        val opponentTextView: TextView = itemView.findViewById(R.id.opponentTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_game, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = games[position]
        holder.opponentTextView.text = p.opponent
        holder.clubTextView.text = Config.club?.name ?: ""
        holder.cardGame.setOnClickListener {
            val context = holder.cardGame.context
            Toast.makeText(context, "pos: "+position, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return games.size
    }
}