package com.example.squadhub.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.squadhub.Config
import com.example.squadhub.Core
import com.example.squadhub.GameStatisticsActivity
import com.example.squadhub.R
import com.example.squadhub.TrainingPerformanceActivity
import com.example.squadhub.model.TrainingSession
import org.json.JSONException
import org.json.JSONObject

class TrainingSessionsAdapter(val trainingSession: ArrayList<TrainingSession>):
    RecyclerView.Adapter<TrainingSessionsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardTraining: CardView = itemView.findViewById(R.id.cardTraining)
        val trainingType: TextView = itemView.findViewById(R.id.itemtraining_training)
        val trainingDate: TextView = itemView.findViewById(R.id.itemtraining_date)
        val trainingTime: TextView = itemView.findViewById(R.id.itemtraining_time)
        val itemtraining_delete: ImageView = itemView.findViewById(R.id.itemtraining_delete)
        val itemtraining_statistics: ImageView = itemView.findViewById(R.id.itemtraining_statistics)
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
        val date = Core.convertFormatDate(t.date)
        holder.trainingDate.text = date
        holder.trainingTime.text = t.startTime + " - " + t.endTime

        holder.itemtraining_delete.setOnClickListener{
            val context = holder.cardTraining.context
            val idTrainingSession = t.id
            deleteDialog(context, idTrainingSession, position)
        }

        holder.itemtraining_statistics.setOnClickListener{
            val context = holder.cardTraining.context
            val intent = Intent(context, TrainingPerformanceActivity::class.java)
            intent.putExtra("idTraining", t.id)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return trainingSession.size
    }

    fun deleteDialog(context: Context, idTrainingSession: Int, position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.dialog_removeTrainingTitle))
        builder.setMessage(context.getString(R.string.dialog_removeTrainingMessage))

        // Botão positivo
        builder.setPositiveButton("Ok") { dialog, _ ->
            deleteTrainingSession(context, idTrainingSession)
            trainingSession.removeAt(position)
            notifyItemRemoved(position)
            dialog.dismiss()
        }

        // Botão negativo
        builder.setNegativeButton(context.getString(R.string.dialog_removeGameCancel)) { dialog, _ ->
            dialog.dismiss()
        }

        // Exibir o diálogo
        val dialog = builder.create()
        dialog.show()
    }

    fun deleteTrainingSession(context: Context, idTrainingSession: Int){

        val url = Config.url +  "route.php"
        // Criar os dados JSON
        val jsonBody = JSONObject()
        try {
            jsonBody.put("idTraining", idTrainingSession)
            Config.club?.let { jsonBody.put("idClub", it.id) }
            jsonBody.put("route", "deleteTrainingSession")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        // Criar o JsonObjectRequest
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,  // Método HTTP
            url,  // URL
            jsonBody,  // Dados JSON a enviar
            { response ->
                // Sucesso
                try {
                    //val status = response.getString("status")
                    val message = response.getString("message")
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                // Erro
                println("Erro na requisição: " + error.message)

                // Verificando se o erro tem um corpo de resposta
                val errorMessage = String(error.networkResponse.data)
                val jsonError = JSONObject(errorMessage)

                // Exibir a mensagem de erro enviada pela API
                val message = jsonError.optString("message", "Erro desconhecido")
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        )
        // Adicionar à fila de requisições
        Volley.newRequestQueue(context).add(jsonObjectRequest)

    }

}