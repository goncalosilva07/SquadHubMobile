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
import com.example.squadhub.R
import com.example.squadhub.RoundedCornersTransformation
import com.example.squadhub.SquadActivity
import com.example.squadhub.model.Game
import com.example.squadhub.model.GameCall
import com.squareup.picasso.Picasso

class GameCallAdapter(val gamecall: ArrayList<GameCall>): RecyclerView.Adapter<GameCallAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardGameCall: CardView = itemView.findViewById(R.id.cardGameCall)
        val gamecall_playerName: TextView = itemView.findViewById(R.id.gamecall_playerName)
        val gamecall_playerPosition: TextView = itemView.findViewById(R.id.gamecall_playerPosition)
        val gamecall_playerFace: ImageView = itemView.findViewById(R.id.gamecall_playerFace)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_gamecall, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gc = gamecall[position]

        if (gc.photo != "null"){
            Picasso.get()
                .load(Config.url_images + gc.photo)
                .transform(RoundedCornersTransformation(100f, 200, 200))
                .into(holder.gamecall_playerFace)
        }else{
            holder.gamecall_playerFace.setImageResource(R.drawable.personicon)
        }

        holder.gamecall_playerName.text = gc.name + " " + gc.surname

        val position: Array<String> = gc.position.split(Regex("\\d")).toTypedArray()
        holder.gamecall_playerPosition.text = position[0].uppercase()

    }

    override fun getItemCount(): Int {
        return gamecall.size
    }
}