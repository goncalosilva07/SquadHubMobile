package com.example.squadhub

import android.content.Context
import com.example.squadhub.model.Club
import com.example.squadhub.model.Permissions

class Config(context : Context) {

    companion object {

        val url = "https://esan-tesp-ds-paw.web.ua.pt/tesp-ds-g28/projeto/api/"

        var isLogged = false

        var idUser: Int = -1
        var username: String = ""
        var idClub: Int? = null
        var role: Int = -1
        lateinit var permissions: List<Permissions>
        var club: Club? = null

    }
}