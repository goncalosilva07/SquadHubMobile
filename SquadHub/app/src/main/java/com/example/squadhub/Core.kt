package com.example.squadhub

import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.Locale

class Core {

    companion object {
        fun convertFormatDate(dateString: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            val date = inputFormat.parse(dateString) // Converte para um objeto Date
            return outputFormat.format(date) // Converte para o novo formato
        }

        fun convertFormatDateToSQLFormat(dateString: String): String {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val date = inputFormat.parse(dateString) // Converte para um objeto Date
            return outputFormat.format(date) // Converte para o novo formato
        }

        fun formatTime(inputTime: String): String {
            // Define o formato de entrada e saída
            val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

            // Faz o parsing da string de entrada e formata para o formato desejado
            val date = inputFormat.parse(inputTime)
            return outputFormat.format(date!!)
        }

        fun getToken(context: Context): String? {
            return context.getSharedPreferences("squadhubSP", Context.MODE_PRIVATE).getString("jwt", "")
        }

        fun tokenError(context: Context) {
            context.getSharedPreferences("squadhubSP", Context.MODE_PRIVATE).edit().putBoolean("login", false).putInt("idUser", -1).putString("jwt", "").apply()

            Config.clean()

            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }

}