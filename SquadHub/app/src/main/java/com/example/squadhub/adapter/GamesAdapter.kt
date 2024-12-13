package com.example.squadhub.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.squadhub.Config
import com.example.squadhub.GameCallActivity
import com.example.squadhub.R
import com.example.squadhub.SquadActivity
import com.example.squadhub.model.Game

class GamesAdapter(val games: ArrayList<Game>): RecyclerView.Adapter<GamesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardGame: CardView = itemView.findViewById(R.id.cardGame)
        val clubTextView: TextView = itemView.findViewById(R.id.clubTextView)
        val opponentTextView: TextView = itemView.findViewById(R.id.opponentTextView)
        val game_competition: TextView = itemView.findViewById(R.id.game_competition)
        val game_date: TextView = itemView.findViewById(R.id.game_date)
        val gamecallBtn: ImageView = itemView.findViewById(R.id.gamecallBtn)
        val gamestatisticsBtn: ImageView = itemView.findViewById(R.id.gamestatisticsBtn)
        val deletegameBtn: ImageView = itemView.findViewById(R.id.deletegameBtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_game, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val g = games[position]
        holder.opponentTextView.text = g.opponent
        holder.clubTextView.text = Config.club?.name ?: ""
        holder.game_competition.text = g.competition
        holder.game_date.text = g.date + " " + g.time

        holder.gamecallBtn.setOnClickListener{
            val context = holder.cardGame.context
            val intent = Intent(context, GameCallActivity::class.java)
            // Passar dados para a nova atividade (opcional)
            intent.putExtra("idGame", g.id)
            context.startActivity(intent) // Iniciar a nova atividade
        }

        /*
        holder.cardGame.setOnClickListener {
            val context = holder.cardGame.context
            Toast.makeText(context, "pos: "+position, Toast.LENGTH_SHORT).show()
        }
        */

    }

    override fun getItemCount(): Int {
        return games.size
    }
}