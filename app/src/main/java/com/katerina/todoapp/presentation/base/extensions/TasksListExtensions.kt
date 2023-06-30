package com.katerina.todoapp.presentation.base.extensions

import android.content.Context
import android.os.Build
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Long.toDateFormat(context: Context): String = when {
    Build.VERSION.SDK_INT >= 26 -> {
        val instant = Instant.ofEpochMilli(this)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
    }

    else -> {
        val date = Date(this)
        val locale = context.resources.configuration.locales.get(0)
        val formatter = SimpleDateFormat("dd.MM.yyyy", locale)

        formatter.timeZone = TimeZone.getDefault()
        formatter.format(date)
    }
}

fun createDateString(day: Int, month: Int, year: Int): String =
    String.format("%02d.%02d.%04d", day, month + 1, year)

fun dateStringToTimestamp(dateString: String): Long {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val date = dateFormat.parse(dateString)
    return date?.time ?: 0L
}