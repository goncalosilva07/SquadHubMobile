package com.example.squadhub.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.squadhub.Config
import com.example.squadhub.GameStatisticsActivity
import com.example.squadhub.PlayerActivity
import com.example.squadhub.R
import com.example.squadhub.RoundedCornersTransformation
import com.example.squadhub.model.User
import com.squareup.picasso.Picasso

class SquadAdapter(val players: ArrayList<User>):
    RecyclerView.Adapter<SquadAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardPlayer: CardView = itemView.findViewById(R.id.cardPlayer)
        val playerName: TextView = itemView.findViewById(R.id.playerName)
        val playerPhoto: ImageView = itemView.findViewById(R.id.playerFace)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_player, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = players[position]

        if (p.photo != "null"){
            Picasso.get()
                .load(Config.url_images + p.photo)
                .transform(RoundedCornersTransformation(100f, 200, 200))
                .into(holder.playerPhoto)
        }else{
            holder.playerPhoto.setImageResource(R.drawable.personicon)
        }

        holder.playerName.text = p.name + " " + p.surname
        holder.cardPlayer.setOnClickListener {
            val context = holder.cardPlayer.context
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra("idUser", p.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return players.size
    }

}