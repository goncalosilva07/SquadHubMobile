package com.example.squadhub

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject

class CoachActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_coach)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getCoach()
    }

    fun goToTeam(view: View){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("selectedItemId", 2)
        startActivity(intent)
        finish()
    }

    fun getCoach(){
        val url = Config.url +  "route.php"
        // Criar os dados JSON
        val jsonBody = JSONObject()
        try {
            Config.club?.let { jsonBody.put("idClub", it.id) }
            jsonBody.put("route", "getCoach")
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

                    if (response.getString("photo") != "null"){
                        // Referencie a ImageView no layout:
                        val imageView = findViewById<ImageView>(R.id.coach_photo)

                        // Use o Picasso para carregar a imagem:
                        Picasso.get()
                            .load(Config.url_images + response.getString("photo")) // URL da imagem
                            .transform(RoundedCornersTransformation(100f, 200, 200))
                            .into(imageView) // ImageView onde a imagem será exibida
                    }

                    //val status = response.getString("status")
                    findViewById<TextView>(R.id.coach_name).text = response.getString("name") + " " + response.getString("surname")
                    findViewById<TextView>(R.id.coach_email).text = response.getString("email")
                    findViewById<TextView>(R.id.coach_phone).text = response.getString("phone")
                    findViewById<TextView>(R.id.coach_birthdate).text = Core.convertFormatDate(response.getString("birthdate"))
                    findViewById<TextView>(R.id.coach_careerStartDate).text = Core.convertFormatDate(response.getString("careerStartDate"))
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