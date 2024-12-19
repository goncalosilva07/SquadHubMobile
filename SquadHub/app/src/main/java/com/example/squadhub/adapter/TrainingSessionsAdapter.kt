package com.example.squadhub.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.squadhub.R
import com.example.squadhub.model.TrainingSession

class TrainingSessionsAdapter(val trainingSession: ArrayList<TrainingSession>):
    RecyclerView.Adapter<TrainingSessionsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardTraining: CardView = itemView.findViewById(R.id.cardTraining)
        val trainingType: TextView = itemView.findViewById(R.id.itemtraining_training)
        val trainingDate: TextView = itemView.findViewById(R.id.itemtraining_date)
        val trainingTime: TextView = itemView.findViewById(R.id.itemtraining_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_training, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val t = trainingSession[position]
        holder.trainingType.text = t.trainingType
        holder.trainingDate.text = t.date
        holder.trainingTime.text = t.startTime + " - " + t.endTime

        holder.cardTraining.setOnClickListener {
            val context = holder.cardTraining.context
            Toast.makeText(context, "pos: "+position, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return trainingSession.size
    }

}