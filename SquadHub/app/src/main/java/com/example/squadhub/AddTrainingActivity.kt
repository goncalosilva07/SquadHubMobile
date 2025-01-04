package com.example.squadhub

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
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

class AddTrainingActivity : AppCompatActivity() {

    var selectedItem = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_training)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spinner: Spinner = findViewById(R.id.spinner)

        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.training_types,
            android.R.layout.simple_spinner_item // Layout padrão para cada item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedItem = parent?.getItemAtPosition(position).toString()
                //Toast.makeText(this@AddTrainingActivity, selectedItem, Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Ação quando nada é selecionado (opcional)
            }
        }

        findViewById<EditText>(R.id.addTraining_date).setOnClickListener {
            // Obter a data atual
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Criar e exibir o DatePickerDialog
            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                findViewById<EditText>(R.id.addTraining_date).setText(selectedDate)
                Toast.makeText(this, selectedDate, Toast.LENGTH_SHORT).show()
            }, year, month, day)

            datePickerDialog.show()
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
                findViewById<EditText>(R.id.addTraining_date).setText(selectedDate)
                Toast.makeText(this, selectedDate, Toast.LENGTH_SHORT).show()
            }, year, month, day)

            datePickerDialog.show()
        }

        findViewById<EditText>(R.id.addTraining_startTime).setOnClickListener {
            // Abrir o TimePickerDialog
            val timePicker = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                // Formatar a hora como HH:mm
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                Toast.makeText(this, selectedTime, Toast.LENGTH_SHORT).show()
                // Definir o texto no EditText
                findViewById<EditText>(R.id.addTraining_startTime).setText(selectedTime)
            }, 12, 0, true) // O último parâmetro 'true' indica formato 24h, use 'false' para 12h.

            timePicker.show()
        }

        findViewById<ImageView>(R.id.startTime).setOnClickListener {
            // Abrir o TimePickerDialog
            val timePicker = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                // Formatar a hora como HH:mm
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                Toast.makeText(this, selectedTime, Toast.LENGTH_SHORT).show()
                // Definir o texto no EditText
                findViewById<EditText>(R.id.addTraining_startTime).setText(selectedTime)
            }, 12, 0, true) // O último parâmetro 'true' indica formato 24h, use 'false' para 12h.

            timePicker.show()
        }

        findViewById<ImageView>(R.id.endTime).setOnClickListener {
            // Abrir o TimePickerDialog
            val timePicker = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                // Formatar a hora como HH:mm
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                Toast.makeText(this, selectedTime, Toast.LENGTH_SHORT).show()
                // Definir o texto no EditText
                findViewById<EditText>(R.id.addTraining_endTime).setText(selectedTime)
            }, 12, 0, true) // O último parâmetro 'true' indica formato 24h, use 'false' para 12h.

            timePicker.show()
        }

        findViewById<EditText>(R.id.addTraining_endTime).setOnClickListener {
            // Abrir o TimePickerDialog
            val timePicker = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                // Formatar a hora como HH:mm
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                Toast.makeText(this, selectedTime, Toast.LENGTH_SHORT).show()
                // Definir o texto no EditText
                findViewById<EditText>(R.id.addTraining_endTime).setText(selectedTime)
            }, 12, 0, true) // O último parâmetro 'true' indica formato 24h, use 'false' para 12h.

            timePicker.show()
        }

    }

    fun goBack(view: View){
        val intent = Intent(this, TrainingSessionActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun addTraining(view: View){

        val dateString = findViewById<EditText>(R.id.addTraining_date).text.toString()
        val startTimeString = findViewById<EditText>(R.id.addTraining_startTime).text.toString()
        val endTimeString = findViewById<EditText>(R.id.addTraining_endTime).text.toString()

        if (selectedItem != "" && dateString != "" && startTimeString != "" && endTimeString != ""){

            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            // Converte a data
            val date = inputFormat.parse(dateString)
            val convertedDate = outputFormat.format(date!!)

            val url = Config.url +  "route.php"
            // Criar os dados JSON
            val jsonBody = JSONObject()
            try {
                jsonBody.put("route", "addTrainingSession")
                jsonBody.put("trainingType", selectedItem)
                jsonBody.put("date", convertedDate)
                jsonBody.put("startTime", startTimeString)
                jsonBody.put("endTime", endTimeString)
                Config.club?.let { jsonBody.put("idClub", it.id) }
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
                        //val status = response.getString("status")
                        val message = response.getString("message")
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

                        val intent = Intent(this, TrainingSessionActivity::class.java)
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