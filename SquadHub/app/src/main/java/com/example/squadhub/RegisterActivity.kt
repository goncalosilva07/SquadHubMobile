package com.example.squadhub

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.squadhub.databinding.ActivityRegisterBinding
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Locale

class RegisterActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    var role = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val extras = intent.extras

        if (extras != null){
            role = extras.getInt("role")
            Toast.makeText(this, role.toString(), Toast.LENGTH_LONG).show()
        }else{
            val intent = Intent(this, UserTypeActivity::class.java)
            startActivity(intent)
        }
    }

    fun register(view: View){

        val username = binding.usernameTxt.text.toString()
        val name = binding.nameTxt.text.toString()
        val surname = binding.surnameTxt.text.toString()
        val email = binding.emailTxt.text.toString()
        val phone = binding.phoneTxt.text.toString()
        val birthdate = binding.birthdateTxt.text.toString()
        val password = binding.passwordTxt.text.toString()
        val repeatPassword = binding.repeatPasswordTxt.text.toString()

        if (password == repeatPassword && password != ""){

            if (username != "" && name != "" && surname != "" && email != "" && phone != "" && birthdate != ""){
                Toast.makeText(this, username, Toast.LENGTH_SHORT).show()

                val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

                // Converte a data
                val date = inputFormat.parse(birthdate)
                val convertedDate = outputFormat.format(date!!)

                val url = Config.url +  "register.php"
                // Criar os dados JSON
                val jsonBody = JSONObject()
                try {
                    jsonBody.put("username", username)
                    jsonBody.put("password", password)
                    jsonBody.put("name", name)
                    jsonBody.put("surname", surname)
                    jsonBody.put("birthDate", convertedDate)
                    jsonBody.put("phone", phone)
                    jsonBody.put("email", email)
                    jsonBody.put("role", role)
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

                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)

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


        }else{
            Toast.makeText(this, "Passwords não coincidem", Toast.LENGTH_SHORT).show()
        }


    }

    fun goToLoginPage(view: View){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}