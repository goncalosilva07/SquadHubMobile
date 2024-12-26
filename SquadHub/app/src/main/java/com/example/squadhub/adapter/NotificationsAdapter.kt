package com.example.squadhub.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.squadhub.R
import com.example.squadhub.model.Notification

class NotificationsAdapter(val notifications: ArrayList<Notification>): RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardNotification: CardView = itemView.findViewById(R.id.cardNotification)
        val title: TextView = itemView.findViewById(R.id.notificationTitle)
        val description: TextView = itemView.findViewById(R.id.notificationDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val n = notifications[position]
        holder.title.text = n.title
        holder.description.text = n.description

        //val position: Array<String> = gc.position.split(Regex("\\d")).toTypedArray()
        //holder.gamecall_playerPosition.text = position[0].uppercase()

    }

    override fun getItemCount(): Int {
        return notifications.size
    }
}