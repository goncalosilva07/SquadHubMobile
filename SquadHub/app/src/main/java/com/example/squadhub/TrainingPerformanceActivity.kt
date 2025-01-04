package com.example.squadhub

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.squadhub.adapter.TrainingPerformanceAdapter
import com.example.squadhub.model.TrainingPerformance
import org.json.JSONException
import org.json.JSONObject

class TrainingPerformanceActivity : AppCompatActivity() {

    private var trainingPerformances = ArrayList<TrainingPerformance>()
    private var idTraining: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_training_performance)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<RecyclerView>(R.id.recyclerViewTrainingPerformance).layoutManager =
            LinearLayoutManager(this)

        idTraining = intent.getIntExtra("idTraining", -1)
        getTrainingPerformances(idTraining)
    }

    fun goBack(view: View){
        val intent = Intent(this, TrainingSessionActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun getTrainingPerformances(idTraining: Int){
        val url = Config.url + "route.php"
        // Criar os dados JSON
        val jsonBody = JSONObject()
        try {
            jsonBody.put("route", "getPlayersTrainingsRatings")
            jsonBody.put("idTraining", idTraining)
            Config.club?.let { jsonBody.put("idClub", it.id) }
            jsonBody.put("idUser", Config.idUser)
            jsonBody.put("jwt", Core.getToken(this))
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
                    for (i in 0 until response.length()) {
                        val item = response.getJSONObject(i)
                        val p = TrainingPerformance(
                            item.getInt("idUser"),
                            item.getInt("idTraining"),
                            item.getInt("idClub"),
                            item.getString("name"),
                            item.getString("surname"),
                            item.getString("photo"),
                            item.getInt("idTrainingPerformance"),
                            item.getString("pontuation"),
                            item.getString("description"),
                        )

                        trainingPerformances.add(p)
                    }

                    findViewById<RecyclerView>(R.id.recyclerViewTrainingPerformance).adapter =
                        TrainingPerformanceAdapter(trainingPerformances)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                // Erro
                println("Erro na requisição: " + error.message)

                val errorMessage = String(error.networkResponse.data)
                val jsonError = JSONObject(errorMessage)

                val message = jsonError.optString("message", "Erro desconhecido")
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
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