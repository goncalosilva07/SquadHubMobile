package com.example.squadhub

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.squadhub.model.Permissions.Companion.parsePermissionsManually
import com.example.squadhub.databinding.ActivityLoginBinding
import com.example.squadhub.model.Club
import org.json.JSONException
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        if (intent.extras != null){
            val id = intent.getIntExtra("id", -1)
            loginSaved(id)
        }
    }

    fun login(view: View){

        val username = binding.txtUsername.text
        val password = binding.txtPassword.text

        //val url = "https://esan-tesp-ds-paw.web.ua.pt/tesp-ds-g28/projeto/api/login.php"
        val url = Config.url +  "login.php"
        // Criar os dados JSON
        val jsonBody = JSONObject()
        try {
            jsonBody.put("username", username)
            jsonBody.put("password", password)
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
                    //val status = response.getString("status")
                    val message = response.getString("message")

                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()

                    val permissionsJSON = response.getJSONArray("permissions")
                    val idUser = response.getInt("idUser")
                    val username = response.getString("username")
                    //val idClub = response.getInt("idClub")
                    val role = response.getInt("role")
                    var clubJSON: JSONObject?

                    try {
                        clubJSON = response.getJSONObject("club")
                    }catch (e: JSONException){
                        clubJSON = null
                    }

                    //val permissions = parsePermissions(permissionsJSON)
                    val permissions = parsePermissionsManually(permissionsJSON)

                    Config.idUser = idUser
                    Config.username = username
                    //Config.idClub = idClub
                    Config.role = role
                    Config.permissions = permissions
                    if (clubJSON != null){
                        Config.club = Club( clubJSON.getInt("id"), clubJSON.getInt("idOwner"),
                            clubJSON.getString("name"), clubJSON.getString("abbreviation"), clubJSON.getInt("victories"),
                            clubJSON.getInt("draws"), clubJSON.getInt("defeats"))
                    }

                    if (binding.saveLogin.isChecked){
                        getSharedPreferences("squadhubSP", Context.MODE_PRIVATE).edit().putBoolean("login", true).putInt("idUser", idUser).apply()
                    }

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()

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

    fun loginSaved(idUser: Int){

        val url = Config.url +  "loginSaved.php"
        // Criar os dados JSON
        val jsonBody = JSONObject()
        try {
            jsonBody.put("idUser", idUser)
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
                    //val status = response.getString("status")
                    val message = response.getString("message")

                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()

                    val permissionsJSON = response.getJSONArray("permissions")
                    val idUser = response.getInt("idUser")
                    val username = response.getString("username")
                    //val idClub = response.getInt("idClub")
                    val role = response.getInt("role")
                    var clubJSON: JSONObject?

                    try {
                        clubJSON = response.getJSONObject("club")
                    }catch (e: JSONException){
                        clubJSON = null
                    }

                    //val permissions = parsePermissions(permissionsJSON)
                    val permissions = parsePermissionsManually(permissionsJSON)

                    Config.idUser = idUser
                    Config.username = username
                    //Config.idClub = idClub
                    Config.role = role
                    Config.permissions = permissions
                    if (clubJSON != null){
                        Config.club = Club( clubJSON.getInt("id"), clubJSON.getInt("idOwner"),
                            clubJSON.getString("name"), clubJSON.getString("abbreviation"), clubJSON.getInt("victories"),
                            clubJSON.getInt("draws"), clubJSON.getInt("defeats"))
                    }

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
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

    fun register(view: View){
        val intent = Intent(this, UserTypeActivity::class.java)
        startActivity(intent)
    }
}

