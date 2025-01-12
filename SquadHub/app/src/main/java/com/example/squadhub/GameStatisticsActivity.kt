package com.example.squadhub

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
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
            Config.club?.let { jsonBody.put("idClub", it.id) }
            jsonBody.put("idUser", Config.idUser)
            jsonBody.put("jwt", Core.getToken(this))
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
                    findViewById<TextView>(R.id.gamestatistics_date).text = Core.convertFormatDate(gameData.getString("date"))

                    if (gameStatistics != null){
                        findViewById<TextView>(R.id.gamestatistics_nostatistics).visibility = View.GONE

                        val possessionBar = findViewById<ProgressBar>(R.id.possession_bar)
                        val possession_txt = findViewById<TextView>(R.id.possession_txt)
                        //val team1Name = findViewById<TextView>(R.id.team1_name)
                        //val team2Name = findViewById<TextView>(R.id.team2_name)

                        // Exemplo de percentagens
                        val possessionTeam1 = gameStatistics.getInt("ball_posession") // Percentagem de posse para a Equipa 1
                        val possessionTeam2 = 100 - possessionTeam1

                        // Atualizar o texto e a barra
                        //team1Name.text = "Equipa 1: $possessionTeam1%"
                        //team2Name.text = "Equipa 2: $possessionTeam2%"
                        possessionBar.progress = possessionTeam1
                        possession_txt.text = possessionTeam1.toString() + "%"

                        findViewById<TextView>(R.id.goals_scored).text = gameStatistics.getInt("goals_scored").toString()
                        findViewById<TextView>(R.id.goals_conceded).text = gameStatistics.getInt("goals_conceded").toString()
                        findViewById<TextView>(R.id.shots).text = gameStatistics.getInt("shots").toString()
                        findViewById<TextView>(R.id.shots_goal).text = gameStatistics.getInt("shots_goal").toString()
                        findViewById<TextView>(R.id.passes).text = gameStatistics.getInt("passes").toString()
                        findViewById<TextView>(R.id.fouls).text = gameStatistics.getInt("fouls").toString()

                    }else{
                        findViewById<TextView>(R.id.ballPossessionTxt).visibility = View.GONE
                        findViewById<ProgressBar>(R.id.possession_bar).visibility = View.GONE
                        findViewById<TextView>(R.id.possession_txt).visibility = View.GONE
                        findViewById<TextView>(R.id.goalsScoredTxt).visibility = View.GONE
                        findViewById<TextView>(R.id.goalsConcededTxt).visibility = View.GONE
                        findViewById<TextView>(R.id.goals_scored).visibility = View.GONE
                        findViewById<TextView>(R.id.goals_conceded).visibility = View.GONE
                        findViewById<TextView>(R.id.shotsTxt).visibility = View.GONE
                        findViewById<TextView>(R.id.shotsGoalTxt).visibility = View.GONE
                        findViewById<TextView>(R.id.shots).visibility = View.GONE
                        findViewById<TextView>(R.id.shots_goal).visibility = View.GONE
                        findViewById<TextView>(R.id.passesTxt).visibility = View.GONE
                        findViewById<TextView>(R.id.foulsTxt).visibility = View.GONE
                        findViewById<TextView>(R.id.passes).visibility = View.GONE
                        findViewById<TextView>(R.id.fouls).visibility = View.GONE
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
                if (message == "Expired token" || message == "Acesso negado"){
                    Toast.makeText(this, "Token inválido ou expirado. Por favor, inicie sessão novamente para continuar.", Toast.LENGTH_LONG).show()
                    Core.tokenError(this)
                }else{
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
            }
        )
        // Adicionar à fila de requisições
        Volley.newRequestQueue(this).add(jsonObjectRequest)

    }
}