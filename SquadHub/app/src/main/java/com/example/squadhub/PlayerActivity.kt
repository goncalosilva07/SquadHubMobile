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
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject

class PlayerActivity : AppCompatActivity() {

    var idUser: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        idUser = intent.getIntExtra("idUser", -1)

        if (Config.role == 3 && idUser != Config.idUser){
            findViewById<ImageView>(R.id.player_performanceBtn).visibility = View.GONE
        }

        getPlayer(idUser)
    }

    fun goBack(view: View){
        val intent = Intent(this, SquadActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun getPlayer(idUser: Int){

        val url = Config.url +  "route.php"
        // Criar os dados JSON
        val jsonBody = JSONObject()
        try {
            jsonBody.put("route", "getPlayer")
            jsonBody.put("idPlayer", idUser)
            jsonBody.put("idUser", Config.idUser)
            Config.club?.let { jsonBody.put("idClub", it.id) }
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
                        val imageView = findViewById<ImageView>(R.id.player_photo)

                        // Use o Picasso para carregar a imagem:
                        Picasso.get()
                            .load(Config.url_images + response.getString("photo")) // URL da imagem
                            .transform(RoundedCornersTransformation(100f, 200, 200))
                            .into(imageView) // ImageView onde a imagem será exibida
                    }

                    findViewById<TextView>(R.id.player_username).text = response.getString("username")

                    findViewById<TextView>(R.id.player_goals).text = response.getString("goals")
                    findViewById<TextView>(R.id.player_assists).text = response.getString("assists")

                    findViewById<TextView>(R.id.player_name).text = response.getString("name") + " " + response.getString("surname")
                    findViewById<TextView>(R.id.player_email).text = response.getString("email")
                    findViewById<TextView>(R.id.player_phone).text = response.getString("phone")
                    findViewById<TextView>(R.id.player_birthdate).text = Core.convertFormatDate(response.getString("birthdate"))
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

    fun playerInjuries(view: View){
        val intent = Intent(this, PlayerInjuryActivity::class.java)
        intent.putExtra("idUser", idUser)
        startActivity(intent)
        finish()
    }

    fun playerPerformance(view: View){
        val intent = Intent(this, PlayerPerformanceActivity::class.java)
        intent.putExtra("idUser", idUser)
        startActivity(intent)
        finish()
    }
}