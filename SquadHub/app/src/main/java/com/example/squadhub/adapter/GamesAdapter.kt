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
import androidx.core.content.ContextCompat.getString
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.squadhub.Config
import com.example.squadhub.GameCallActivity
import com.example.squadhub.GameStatisticsActivity
import com.example.squadhub.LoginActivity
import com.example.squadhub.R
import com.example.squadhub.SquadActivity
import com.example.squadhub.model.Game
import org.json.JSONException
import org.json.JSONObject

class GamesAdapter(val games: ArrayList<Game>): RecyclerView.Adapter<GamesAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val cardGame: CardView = itemView.findViewById(R.id.cardGame)
        val clubTextView: TextView = itemView.findViewById(R.id.clubTextView)
        val opponentTextView: TextView = itemView.findViewById(R.id.opponentTextView)
        val game_competition: TextView = itemView.findViewById(R.id.game_competition)
        val game_date: TextView = itemView.findViewById(R.id.game_date)
        val gamecallBtn: ImageView = itemView.findViewById(R.id.gamecallBtn)
        val gamestatisticsBtn: ImageView = itemView.findViewById(R.id.gamestatisticsBtn)
        val deletegameBtn: ImageView = itemView.findViewById(R.id.deletegameBtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_game, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val g = games[position]
        holder.opponentTextView.text = g.opponent
        holder.clubTextView.text = Config.club?.name ?: ""
        holder.game_competition.text = g.competition
        holder.game_date.text = g.date + " " + g.time

        holder.gamecallBtn.setOnClickListener{
            val context = holder.cardGame.context
            val intent = Intent(context, GameCallActivity::class.java)
            // Passar dados para a nova atividade (opcional)
            intent.putExtra("idGame", g.id)
            context.startActivity(intent) // Iniciar a nova atividade
        }

        holder.deletegameBtn.setOnClickListener{
            val context = holder.cardGame.context
            val idGame = g.id
            deleteDialog(context, idGame, position)
        }

        holder.gamestatisticsBtn.setOnClickListener{
            val context = holder.cardGame.context
            val intent = Intent(context, GameStatisticsActivity::class.java)
            intent.putExtra("idGame", g.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return games.size
    }

    fun deleteDialog(context: Context, idGame: Int, position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.dialog_removeGameTitle))
        builder.setMessage(context.getString(R.string.dialog_removeGameMessage))

        // Botão positivo
        builder.setPositiveButton("Ok") { dialog, _ ->
            deleteGame(context ,idGame)
            games.removeAt(position)
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

    fun deleteGame(context: Context, idGame: Int){

        val url = Config.url +  "route.php"
        // Criar os dados JSON
        val jsonBody = JSONObject()
        try {
            jsonBody.put("idGame", idGame)
            jsonBody.put("route", "deleteGame")
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