package com.example.squadhub

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.squadhub.adapter.GamesAdapter
import com.example.squadhub.adapter.TrainingSessionsAdapter
import com.example.squadhub.model.Game
import com.example.squadhub.model.TrainingSession
import org.json.JSONException
import org.json.JSONObject

class TrainingSessionActivity : AppCompatActivity() {

    private var trainingSessions = ArrayList<TrainingSession>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_training_session)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<RecyclerView>(R.id.recyclerViewTraining).layoutManager =
            LinearLayoutManager(this)

        getTrainingSessions()
    }

    fun goToTeam(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun getTrainingSessions(){

        val url = Config.url + "route.php"
        // Criar os dados JSON
        val jsonBody = JSONObject()
        try {
            Config.club?.let { jsonBody.put("idClub", it.id.toString()) }
            jsonBody.put("route", "getTrainingSessions")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        // Criar o JsonObjectRequest
        val jsonObjectRequest = object : JsonArrayRequest(
            Request.Method.POST,  // Método HTTP
            url,  // URL
            null,  // Dados JSON a enviar
            { response ->
                // Sucesso
                try {
                    //val jsonObject: JSONArray = response
                    println(response)

                    //val jsonArray = JSONArray(response)
                    for(i in 0 until response.length()) {
                        val item = response.getJSONObject(i)
                        val t = TrainingSession(item.getInt("id"),
                            item.getInt("idClub"),
                            )

                        trainingSessions.add(t)
                    }

                    findViewById<RecyclerView>(R.id.recyclerViewTraining).adapter =
                        TrainingSessionsAdapter(trainingSessions)

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

            }
        ){
            override fun getBody(): ByteArray {
                return jsonBody.toString().toByteArray(Charsets.UTF_8)
            }

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }
        // Adicionar à fila de requisições
        Volley.newRequestQueue(this).add(jsonObjectRequest)

    }
}