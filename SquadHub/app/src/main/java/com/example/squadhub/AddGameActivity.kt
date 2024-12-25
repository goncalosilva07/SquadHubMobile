package com.example.squadhub

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddGameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_game)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        findViewById<ImageView>(R.id.calendar).setOnClickListener {
            // Obter a data atual
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Criar e exibir o DatePickerDialog
            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                findViewById<EditText>(R.id.addGame_date).setText(selectedDate)
                Toast.makeText(this, selectedDate, Toast.LENGTH_SHORT).show()
            }, year, month, day)

            datePickerDialog.show()
        }

        findViewById<ImageView>(R.id.hour).setOnClickListener {
            // Abrir o TimePickerDialog
            val timePicker = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                // Formatar a hora como HH:mm
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                Toast.makeText(this, selectedTime, Toast.LENGTH_SHORT).show()
                // Definir o texto no EditText
                findViewById<EditText>(R.id.addGame_time).setText(selectedTime)
            }, 12, 0, true) // O último parâmetro 'true' indica formato 24h, use 'false' para 12h.

            timePicker.show()
        }
    }

    fun goBack(view: View){
        val intent = Intent(this, GamesActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun addGame(view: View){

        val opponent = findViewById<EditText>(R.id.addGame_opponent).text.toString()
        val competition = findViewById<EditText>(R.id.addGame_competition).text.toString()
        val local = findViewById<EditText>(R.id.addGame_local).text.toString()
        val dateString = findViewById<EditText>(R.id.addGame_date).text.toString()
        val timeString = findViewById<EditText>(R.id.addGame_time).text.toString()

        if (opponent != "" && competition != "" && local != "" && dateString != "" && timeString != ""){


            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            // Converte a data
            val date = inputFormat.parse(dateString)
            val convertedDate = outputFormat.format(date!!)

            val url = Config.url +  "route.php"
            // Criar os dados JSON
            val jsonBody = JSONObject()
            try {
                jsonBody.put("route", "addGame")
                jsonBody.put("opponent", opponent)
                jsonBody.put("competition", competition)
                jsonBody.put("local", local)
                jsonBody.put("date", convertedDate)
                jsonBody.put("time", timeString)
                Config.club?.let { jsonBody.put("idClub", it.id) }
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

                        val intent = Intent(this, GamesActivity::class.java)
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




    }
}