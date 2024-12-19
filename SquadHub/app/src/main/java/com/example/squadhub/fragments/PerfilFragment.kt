package com.example.squadhub.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.squadhub.Config
import com.example.squadhub.LoginActivity
import com.example.squadhub.MainActivity
import com.example.squadhub.R
import com.example.squadhub.model.Club
import com.example.squadhub.model.Permissions.Companion.parsePermissionsManually
import org.json.JSONException
import org.json.JSONObject

class PerfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getPerfilData()

        view.findViewById<Button>(R.id.logout).setOnClickListener {
            logout(view)
        }

        view.findViewById<Button>(R.id.saveBtn).setOnClickListener {
            updateUserData()
        }
    }

    fun getPerfilData(){

        val url = Config.url +  "route.php"
        // Criar os dados JSON
        val jsonBody = JSONObject()
        try {
            jsonBody.put("idUser", Config.idUser)
            jsonBody.put("route", "getUserData")
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

                    requireView().findViewById<TextView>(R.id.username).text = response.getString("username")
                    requireView().findViewById<TextView>(R.id.nameEditText).text = response.getString("name")
                    requireView().findViewById<TextView>(R.id.surnameEditText).text = response.getString("surname")
                    requireView().findViewById<TextView>(R.id.birthdateEditText).text = response.getString("birthdate")
                    requireView().findViewById<TextView>(R.id.emailEditText).text = response.getString("email")
                    requireView().findViewById<TextView>(R.id.phoneEditText).text = response.getString("phone")

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
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        )
        // Adicionar à fila de requisições
        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest)
    }

    fun updateUserData(){

        val nameEditText = requireView().findViewById<EditText>(R.id.nameEditText).text
        val surnameEditText = requireView().findViewById<EditText>(R.id.surnameEditText).text
        val emailEditText = requireView().findViewById<EditText>(R.id.emailEditText).text
        val phoneEditText = requireView().findViewById<EditText>(R.id.phoneEditText).text
        val birthdateEditText = requireView().findViewById<EditText>(R.id.birthdateEditText).text

        val url = Config.url +  "route.php"
        // Criar os dados JSON
        val jsonBody = JSONObject()
        try {
            jsonBody.put("idUser", Config.idUser)
            jsonBody.put("name", nameEditText)
            jsonBody.put("surname", surnameEditText)
            jsonBody.put("email", emailEditText)
            jsonBody.put("phone", phoneEditText)
            jsonBody.put("birthdate", birthdateEditText)
            jsonBody.put("route", "updateUserData")
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
                    Toast.makeText(requireContext(), response.getString("message"), Toast.LENGTH_LONG).show()
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
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        )
        // Adicionar à fila de requisições
        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest)
    }

    fun logout(view: View){
        view.context.getSharedPreferences("squadhubSP", Context.MODE_PRIVATE).edit().putBoolean("login", false).putInt("idUser", -1).apply()

        Config.clean()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        //requireContext().getSharedPreferences("squadhubSP", Context.MODE_PRIVATE).edit().putBoolean("login", false).putInt("idUser", -1).apply()
    }
}