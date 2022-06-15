package com.betterlifeapps.chessclock.domain

data class PlayerState(val timeMillis: Long, val turn: Int)

val PlayerState.formattedTime: String
    get() {
        var minutes = (timeMillis / 60 / 1000).toString()
        if (minutes.length == 1) minutes = "0$minutes"
        var seconds = ((timeMillis / 1000) % 60).toString()
        if (seconds.length == 1) seconds = "0$seconds"
        return "$minutes:$seconds"
    }