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

class StaffPerfilActivity : AppCompatActivity() {

    var idUser: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_staff_perfil)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        idUser = intent.getIntExtra("idUser", -1)
        getStaff(idUser)
    }

    fun goBack(view: View){
        val intent = Intent(this, StaffActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun getStaff(idUser: Int){
        val url = Config.url +  "route.php"
        // Criar os dados JSON
        val jsonBody = JSONObject()
        try {
            jsonBody.put("route", "getStaffPerfil")
            jsonBody.put("idUser", Config.idUser)
            jsonBody.put("idStaff", idUser)
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
                        val imageView = findViewById<ImageView>(R.id.staff_photo)

                        // Use o Picasso para carregar a imagem:
                        Picasso.get()
                            .load(Config.url_images + response.getString("photo")) // URL da imagem
                            .transform(RoundedCornersTransformation(100f, 200, 200))
                            .into(imageView) // ImageView onde a imagem será exibida
                    }

                    val birthdate = Core.convertFormatDate(response.getString("birthdate"))

                    findViewById<TextView>(R.id.staff_name).text = response.getString("name") + " " + response.getString("surname")
                    findViewById<TextView>(R.id.staff_email).text = response.getString("email")
                    findViewById<TextView>(R.id.staff_phone).text = response.getString("phone")
                    findViewById<TextView>(R.id.staff_birthdate).text = birthdate
                    findViewById<TextView>(R.id.staff_careerStartDate).text = if (response.getString("careerStartDate") == "null") "" else Core.convertFormatDate(response.getString("careerStartDate"))
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