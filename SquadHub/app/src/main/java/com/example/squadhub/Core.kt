package com.example.squadhub

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
    }

}