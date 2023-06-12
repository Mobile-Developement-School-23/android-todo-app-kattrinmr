package com.katerina.todoapp.presentation.extensions

import android.content.Context
import android.os.Build
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.TimeZone

fun View.visible() {
    isVisible = true
}

fun View.invisible() {
    isVisible = false
}

fun View.gone() {
    isGone = true
}

fun Long.toDateFormat(context: Context): String = when {
    Build.VERSION.SDK_INT >= 26 -> {
        val instant = Instant.ofEpochSecond(this)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    else -> {
        val date = Date(this * 1000)
        val locale = context.resources.configuration.locales.get(0)
        val formatter = SimpleDateFormat("yyyy-MM-dd", locale)

        formatter.timeZone = TimeZone.getDefault()
        formatter.format(date)
    }
}

fun View.snackbar(
    message: String,
    duration: Int = Snackbar.LENGTH_LONG
) {
    Snackbar.make(this, message, duration).show()
}

fun showSystemMessage(view: View, message: String) {
    view.snackbar(message)
}