package com.example.squadhub.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.squadhub.Core
import com.example.squadhub.R
import com.example.squadhub.model.Injury

class InjuryAdapter(val injuries: ArrayList<Injury>):
    RecyclerView.Adapter<InjuryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardInjury: CardView = itemView.findViewById(R.id.cardInjury)
        val injury_title: TextView = itemView.findViewById(R.id.injury_title)
        val injury_date: TextView = itemView.findViewById(R.id.injury_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_injury, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val i = injuries[position]

        val context = holder.cardInjury.context

        holder.injury_title.text = i.injury

        val startDate = Core.convertFormatDate(i.startDate)
        var endDate = ""
        if (i.endDate == "null"){
            endDate = context.getString(R.string.no_endDate)
        }else{
            endDate = Core.convertFormatDate(i.endDate)
        }

        holder.injury_date.text = startDate + " - " + endDate
    }

    override fun getItemCount(): Int {
        return injuries.size
    }
}