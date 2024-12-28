package com.example.squadhub.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.squadhub.Config
import com.example.squadhub.R
import com.example.squadhub.adapter.NotificationsAdapter
import com.example.squadhub.adapter.SquadAdapter
import com.example.squadhub.model.Notification
import com.example.squadhub.model.User
import org.json.JSONException
import org.json.JSONObject

class NotificationsFragment : Fragment() {

    private var notifications = ArrayList<Notification>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireView().findViewById<RecyclerView>(R.id.recyclerViewNotifications).layoutManager = LinearLayoutManager(requireContext())
        getNotifications()
    }

    fun getNotifications(){

        val url = Config.url +  "route.php"
        // Criar os dados JSON
        val jsonBody = JSONObject()
        try {
            jsonBody.put("idUser", Config.idUser)
            jsonBody.put("route", "getNotifications")
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
                        requireView().findViewById<RecyclerView>(R.id.recyclerViewNotifications).visibility = View.GONE
                    }else {
                        requireView().findViewById<CardView>(R.id.notification_noNotification).visibility = View.GONE
                        //val jsonArray = JSONArray(response)
                        for (i in 0 until response.length()) {
                            val item = response.getJSONObject(i)
                            val n = Notification(
                                item.getInt("id"),
                                item.getInt("idClub"),
                                item.getString("title"),
                                item.getString("description"),
                                item.getBoolean("isInvite")
                            )

                            notifications.add(n)
                        }
                        requireView().findViewById<RecyclerView>(R.id.recyclerViewNotifications).adapter =
                            NotificationsAdapter(notifications)
                    }

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
        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest)
    }

}