package com.example.squadhub.model

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray

class Permissions (val id: Int,
                   val name: String,
                   val description: String,
                   val icon: String,
                   val isMenu: Boolean){

    companion object {
        //Função para converter um JSONArray para uma lista do tipo permissions
        fun parsePermissions(jsonArray: JSONArray): List<Permissions> {
            val gson = Gson()
            val listType = object : TypeToken<List<Permissions>>() {}.type
            return gson.fromJson(jsonArray.toString(), listType)
        }

        fun parsePermissionsManually(jsonArray: JSONArray): List<Permissions> {
            val permissionsList = mutableListOf<Permissions>()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val permission = Permissions(
                    id = jsonObject.getInt("id"),
                    name = jsonObject.getString("name"),
                    description = jsonObject.getString("description"),
                    isMenu = jsonObject.getBoolean("isMenu"),
                    icon = jsonObject.getString("icon")
                )
                permissionsList.add(permission)
            }
            return permissionsList
        }

    }


}

