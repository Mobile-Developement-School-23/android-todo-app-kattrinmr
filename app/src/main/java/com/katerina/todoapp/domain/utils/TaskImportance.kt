package com.katerina.todoapp.domain.utils

import android.content.Context
import com.katerina.todoapp.R

enum class TaskImportance {
    LOW, NORMAL, HIGH;

    fun getLocalName(context: Context): String {
        return when (this) {
            LOW -> context.resources.getString(R.string.txt_low_importance)
            NORMAL -> context.resources.getString(R.string.txt_normal_importance)
            HIGH -> context.resources.getString(R.string.txt_high_importance)
        }
    }
}