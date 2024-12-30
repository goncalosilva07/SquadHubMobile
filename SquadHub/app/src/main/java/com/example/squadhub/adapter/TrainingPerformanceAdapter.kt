package com.example.squadhub.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.squadhub.R
import com.example.squadhub.model.TrainingPerformance

class TrainingPerformanceAdapter(val trainingPerformances: ArrayList<TrainingPerformance>): RecyclerView.Adapter<TrainingPerformanceAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardTrainingPerformance: CardView = itemView.findViewById(R.id.cardTrainingPerformance)
        val itemTrainingPerformance_name: TextView = itemView.findViewById(R.id.itemTrainingPerformance_name)
        val itemTrainingPerformance_description: TextView = itemView.findViewById(R.id.itemTrainingPerformance_description)
        val itemTrainingPerformance_performance: TextView = itemView.findViewById(R.id.itemTrainingPerformance_performance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_trainingperformance, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = trainingPerformances[position]
        val context = holder.cardTrainingPerformance.context
        holder.itemTrainingPerformance_name.text = p.name + " " + p.surname
        holder.itemTrainingPerformance_performance.text = if (p.pontuation == "null") context.getString(R.string.no_data) else p.pontuation
        holder.itemTrainingPerformance_description.text = if (p.description == "null" || p.description == "") context.getString(R.string.no_data) else (if (p.description.length > 40) p.description.substring(0, 40) + "..." else p.description)
        //holder.description.text = n.description
    }

    override fun getItemCount(): Int {
        return trainingPerformances.size
    }
}