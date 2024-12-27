package com.example.squadhub.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getString
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.squadhub.Config
import com.example.squadhub.R
import com.example.squadhub.model.Notification
import org.json.JSONException
import org.json.JSONObject

class NotificationsAdapter(val notifications: ArrayList<Notification>): RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardNotification: CardView = itemView.findViewById(R.id.cardNotification)
        val title: TextView = itemView.findViewById(R.id.notificationTitle)
        val description: TextView = itemView.findViewById(R.id.notificationDescription)
        val deleteIcon: ImageView = itemView.findViewById(R.id.deleteNotification)
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

        holder.deleteIcon.setOnClickListener{
            val context = holder.cardNotification.context
            val idNotification = n.id
            deleteDialog(context, idNotification, position)
        }

        holder.cardNotification.setOnClickListener{
            if (n.isInvite){
                val context = holder.cardNotification.context
                Toast.makeText(context, context.getString(R.string.only_web_application), Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    fun deleteDialog(context: Context, idNotification: Int, position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.dialog_removeNotificationTitle))
        builder.setMessage(context.getString(R.string.dialog_removeNotificationMessage))

        // Botão positivo
        builder.setPositiveButton("Ok") { dialog, _ ->
            deleteNotification(context ,idNotification)
            notifications.removeAt(position)
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

    fun deleteNotification(context: Context, idNotification: Int){

        val url = Config.url +  "route.php"
        // Criar os dados JSON
        val jsonBody = JSONObject()
        try {
            jsonBody.put("id", idNotification)
            jsonBody.put("idUser", Config.idUser)
            jsonBody.put("route", "deleteNotification")
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