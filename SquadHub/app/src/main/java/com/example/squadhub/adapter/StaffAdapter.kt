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
import com.example.squadhub.PlayerActivity
import com.example.squadhub.R
import com.example.squadhub.StaffPerfilActivity
import com.example.squadhub.model.User

class StaffAdapter(val staff: ArrayList<User>):
    RecyclerView.Adapter<StaffAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardStaff: CardView = itemView.findViewById(R.id.cardStaff)
        val staffName: TextView = itemView.findViewById(R.id.staffName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_staff, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val s = staff[position]
        holder.staffName.text = s.name
        holder.cardStaff.setOnClickListener {
            val context = holder.cardStaff.context
            val intent = Intent(context, StaffPerfilActivity::class.java)
            intent.putExtra("idUser", s.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return staff.size
    }

}