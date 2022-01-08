package com.betterlifeapps.chessclock.domain

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class PlayerState(val timeMillis: Long, val turn: Int)

val PlayerState.formattedTime: String
    get() = SimpleDateFormat("m:ss", Locale.getDefault()).format(Date(timeMillis))