package com.example.squadhub

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.squadhub.adapter.InjuryAdapter
import com.example.squadhub.adapter.PerformanceAdapter
import com.example.squadhub.model.Injury
import com.example.squadhub.model.Performance
import org.json.JSONException
import org.json.JSONObject

class PlayerPerformanceActivity : AppCompatActivity() {

    private var performances = ArrayList<Performance>()
    private var idUser: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player_performance)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<RecyclerView>(R.id.recyclerViewPerformance).layoutManager =
            LinearLayoutManager(this)

        idUser = intent.getIntExtra("idUser", -1)
        getPerformances(idUser)

    }

    fun goBack(view: View){
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra("idUser", idUser)
        startActivity(intent)
        finish()
    }

    fun getPerformances(idUser: Int){

        val url = Config.url + "route.php"
        // Criar os dados JSON
        val jsonBody = JSONObject()
        try {
            jsonBody.put("route", "getPlayerPerformance")
            jsonBody.put("idUser", idUser)
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
                    if (response.length() == 0){
                        findViewById<RecyclerView>(R.id.recyclerViewPerformance).visibility = View.GONE
                    }else {
                        findViewById<CardView>(R.id.performance_noperformance).visibility = View.GONE
                        //val jsonArray = JSONArray(response)
                        for (i in 0 until response.length()) {
                            val item = response.getJSONObject(i)
                            val p = Performance(
                                item.getString("date"),
                                item.getString("trainingType"),
                                item.getString("pontuation"),
                                item.getString("description")
                            )

                            performances.add(p)
                        }

                        findViewById<RecyclerView>(R.id.recyclerViewPerformance).adapter =
                            PerformanceAdapter(performances)
                    }

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