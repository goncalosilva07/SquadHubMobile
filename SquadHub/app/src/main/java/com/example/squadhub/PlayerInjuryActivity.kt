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
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.squadhub.adapter.InjuryAdapter
import com.example.squadhub.model.Injury
import org.json.JSONException
import org.json.JSONObject

class PlayerInjuryActivity : AppCompatActivity() {

    private var injuries = ArrayList<Injury>()
    private var idUser: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player_injury)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<RecyclerView>(R.id.recyclerViewInjuries).layoutManager =
            LinearLayoutManager(this)

        idUser = intent.getIntExtra("idUser", -1)
        getInjuries(idUser)
    }

    fun goBack(view: View){
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra("idUser", idUser)
        startActivity(intent)
        finish()
    }

    fun getInjuries(idUser: Int){

        val url = Config.url + "route.php"
        // Criar os dados JSON
        val jsonBody = JSONObject()
        try {
            jsonBody.put("route", "getPlayerInjuries")
            jsonBody.put("idUser", Config.idUser)
            jsonBody.put("idPlayer", idUser)
            Config.club?.let { jsonBody.put("idClub", it.id) }
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

                    if (response.length() == 0){
                        findViewById<RecyclerView>(R.id.recyclerViewInjuries).visibility = View.GONE
                    }else{
                        findViewById<CardView>(R.id.injury_noinjury).visibility = View.GONE
                        //val jsonArray = JSONArray(response)
                        for(i in 0 until response.length()) {
                            val item = response.getJSONObject(i)
                            val i = Injury( item.getString("injury"),
                                item.getString("startDate"),
                                item.getString("endDate"))

                            injuries.add(i)
                        }

                        findViewById<RecyclerView>(R.id.recyclerViewInjuries).adapter =
                            InjuryAdapter(injuries)
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
                if (message == "Expired token" || message == "Acesso negado"){
                    Toast.makeText(this, "Token inválido ou expirado. Por favor, inicie sessão novamente para continuar.", Toast.LENGTH_LONG).show()
                    Core.tokenError(this)
                }else{
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                }
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