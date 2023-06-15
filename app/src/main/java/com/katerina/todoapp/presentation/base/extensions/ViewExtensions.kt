package com.katerina.todoapp.presentation.base.extensions

import android.app.DatePickerDialog
import android.content.Context
import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.katerina.todoapp.R
import java.util.Calendar

fun View.visible() {
    isVisible = true
}

fun View.invisible() {
    isVisible = false
}

fun View.gone() {
    isGone = true
}

fun View.disable() {
    isEnabled = false
}

fun View.enable() {
    isEnabled = true
}

fun View.snackbar(
    message: String,
    duration: Int = Snackbar.LENGTH_LONG
) {
    Snackbar.make(this, message, duration).show()
}

fun Context.createDatePicker(listener: DatePickerDialog.OnDateSetListener): DatePickerDialog {
    val calendar = Calendar.getInstance()

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    return DatePickerDialog(this, R.style.DatePickerDialogTheme, listener, year, month, day)
}

fun showSystemMessage(view: View, message: String) {
    view.snackbar(message)
}