package com.example.squadhub.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.squadhub.Config
import com.example.squadhub.Core
import com.example.squadhub.MainActivity
import com.example.squadhub.R
import com.example.squadhub.model.Club
import com.example.squadhub.model.Game
import com.example.squadhub.model.Permissions.Companion.parsePermissionsManually
import org.json.JSONException
import org.json.JSONObject
import org.w3c.dom.Text

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getHomeData()
    }

    fun getHomeData(){

        val url = Config.url + "route.php"
        // Criar os dados JSON
        val jsonBody = JSONObject()
        try {
            Config.club?.let { jsonBody.put("idClub", it.id.toString()) }
            jsonBody.put("route", "getHomeInitialData")
            jsonBody.put("idUser", Config.idUser)
            jsonBody.put("jwt", Core.getToken(requireContext()))
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
                    val jsonObject: JSONObject = response

                    if (jsonObject.optJSONObject("game") != null){

                        var game: Game = Game(response.getJSONObject("game").getInt("id"), response.getJSONObject("game").getInt("idClub"),
                            response.getJSONObject("game").getString("opponent"),
                            response.getJSONObject("game").getString("date"),
                            response.getJSONObject("game").getString("time"),
                            response.getJSONObject("game").getString("competition"),
                            response.getJSONObject("game").getString("local"))

                        requireView().findViewById<TextView>(R.id.noGames).visibility = View.GONE

                        requireView().findViewById<TextView>(R.id.clubName).text = Config.club?.name ?: ""
                        requireView().findViewById<TextView>(R.id.competition).text = game.competition
                        requireView().findViewById<TextView>(R.id.opponent).text = game.opponent
                        requireView().findViewById<TextView>(R.id.local).text = game.local
                        requireView().findViewById<TextView>(R.id.date_time).text = Core.convertFormatDate(game.date) + " " + Core.formatTime(game.time)
                    }else{
                        requireView().findViewById<TextView>(R.id.clubName).visibility = View.GONE
                        requireView().findViewById<TextView>(R.id.competition).visibility = View.GONE
                        requireView().findViewById<TextView>(R.id.opponent).visibility = View.GONE
                        requireView().findViewById<TextView>(R.id.local).visibility = View.GONE
                        requireView().findViewById<TextView>(R.id.date_time).visibility = View.GONE
                        requireView().findViewById<TextView>(R.id.vsTextView).visibility = View.GONE
                        requireView().findViewById<TextView>(R.id.noGames).visibility = View.VISIBLE
                    }

                    requireView().findViewById<TextView>(R.id.clubVictories).text = response.getJSONObject("clubStats").getString("victories")
                    requireView().findViewById<TextView>(R.id.clubDraws).text = response.getJSONObject("clubStats").getString("draws")
                    requireView().findViewById<TextView>(R.id.clubDefeats).text = response.getJSONObject("clubStats").getString("defeats")

                    if(jsonObject.optJSONObject("scorer") != null){
                        requireView().findViewById<TextView>(R.id.scorerName).text = response.getJSONObject("scorer").getString("name") + " " + response.getJSONObject("scorer").getString("surname")
                        requireView().findViewById<TextView>(R.id.scorerGoals).text = response.getJSONObject("scorer").getInt("goals").toString()
                        requireView().findViewById<TextView>(R.id.assisterName).text = response.getJSONObject("assister").getString("name") + " " + response.getJSONObject("assister").getString("surname")
                        requireView().findViewById<TextView>(R.id.assisterAssists).text = response.getJSONObject("assister").getInt("assists").toString()
                    }else{
                        requireView().findViewById<TextView>(R.id.scorerName).text = getString(R.string.noPlayerTextView)
                        requireView().findViewById<TextView>(R.id.assisterName).text = getString(R.string.noPlayerTextView)
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
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        )
        // Adicionar à fila de requisições
        Volley.newRequestQueue(requireContext()).add(jsonObjectRequest)

    }
}