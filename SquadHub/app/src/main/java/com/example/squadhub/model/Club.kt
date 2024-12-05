package com.example.squadhub.model

import org.json.JSONArray

class Club (
    var id: Int,
    var idOwner: Int,
    var name: String,
    var abbreviation: String,
    var victories: Int,
    var draws: Int,
    var defeats: Int
) {

}