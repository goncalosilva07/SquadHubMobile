package com.example.squadhub

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.squadhub.adapter.GameCallAdapter
import com.example.squadhub.model.GameCall
import org.json.JSONException
import org.json.JSONObject

class GameCallActivity : AppCompatActivity() {

    private var gamecall = ArrayList<GameCall>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_call)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val idGame = intent.getIntExtra("idGame", -1)

        findViewById<RecyclerView>(R.id.recyclerViewGameCall).layoutManager =
            LinearLayoutManager(this)

        getGameCall(idGame)
    }

    fun goBack(view: View){
        val intent = Intent(this, GamesActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun getGameCall(idGame: Int){

        val url = Config.url + "route.php"
        // Criar os dados JSON
        val jsonBody = JSONObject()
        try {
            jsonBody.put("route", "getGameCallData")
            jsonBody.put("idGame", idGame)
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
                    //val jsonObject: JSONArray = response
                    println(response)

                    val isCreated = response.getBoolean("isCreated")

                    if (isCreated){
                        findViewById<CardView>(R.id.gamecall_nogamecall).visibility = View.GONE

                        val playersList = response.getJSONArray("playersList")

                        //val jsonArray = JSONArray(response)

                        for(i in 0 until playersList.length()) {
                            val item = playersList.getJSONObject(i)
                            val g = GameCall(item.getInt("id"),
                                item.getInt("idUser"),
                                item.getString("name"),
                                item.getString("surname"),
                                item.getString("photo"),
                                item.getString("position"))
                                gamecall.add(g)
                        }

                        findViewById<RecyclerView>(R.id.recyclerViewGameCall).adapter = GameCallAdapter(gamecall)

                    }else{
                        findViewById<RecyclerView>(R.id.recyclerViewGameCall).visibility = View.GONE
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