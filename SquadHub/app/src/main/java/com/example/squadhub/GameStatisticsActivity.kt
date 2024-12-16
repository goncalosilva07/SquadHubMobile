package com.example.squadhub

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Text

class GameStatisticsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_statistics)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val idGame = intent.getIntExtra("idGame", -1)

        getGameStatistics(idGame)
    }

    fun goBack(view: View){
        val intent = Intent(this, GamesActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun getGameStatistics(idGame: Int){

        val url = Config.url +  "route.php"
        // Criar os dados JSON
        val jsonBody = JSONObject()
        try {
            jsonBody.put("idGame", idGame)
            jsonBody.put("route", "getGameStatistics")
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

                    val gameData = response.getJSONObject("gameData")
                    val gameScorers = response.getJSONArray("gameScorers")
                    val gameStatistics = response.optJSONObject("gameStatistics")

                    findViewById<TextView>(R.id.gamestatistics_competition).text = gameData.getString("competition")
                    findViewById<TextView>(R.id.gamestatistics_club).text = Config.club?.name ?: ""
                    findViewById<TextView>(R.id.gamestatistics_opponent).text = gameData.getString("opponent")
                    findViewById<TextView>(R.id.gamestatistics_local).text = gameData.getString("local")
                    findViewById<TextView>(R.id.gamestatistics_date).text = gameData.getString("date")

                    if (gameStatistics != null){
                        findViewById<TextView>(R.id.gamestatistics_nostatistics).visibility = View.GONE
                    }else{

                    }

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
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        )
        // Adicionar à fila de requisições
        Volley.newRequestQueue(this).add(jsonObjectRequest)

    }
}