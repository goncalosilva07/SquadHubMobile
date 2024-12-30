package com.example.squadhub.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.squadhub.Core
import com.example.squadhub.R
import com.example.squadhub.model.Performance

class PerformanceAdapter(val performances: ArrayList<Performance>):
    RecyclerView.Adapter<PerformanceAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardPerformance: CardView = itemView.findViewById(R.id.cardPerformance)
        val performance_date: TextView = itemView.findViewById(R.id.performance_date)
        val performance_trainingType_pontuation: TextView = itemView.findViewById(R.id.performance_trainingType_pontuation)
        val performance_description: TextView = itemView.findViewById(R.id.performance_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_performance, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = performances[position]

        val context = holder.cardPerformance.context

        val date = Core.convertFormatDate(p.date)
        holder.performance_date.text = date

        holder.performance_trainingType_pontuation.text = p.trainingType + " - " + p.pontuation
        holder.performance_description.text = if(p.description == "" || p.description == "null") context.getString(R.string.no_data) else p.description
    }

    override fun getItemCount(): Int {
        return performances.size
    }
}