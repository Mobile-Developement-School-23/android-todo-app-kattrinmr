package com.katerina.todoapp.domain.utils

import android.content.Context
import com.katerina.todoapp.R
import com.katerina.todoapp.data.api.serializers.TaskImportanceSerializer
import kotlinx.serialization.Serializable

@Serializable(with = TaskImportanceSerializer::class)
enum class TaskImportance {
    LOW, NORMAL, HIGH;

    fun getLocalName(context: Context): String {
        return when (this) {
            LOW -> context.resources.getString(R.string.txt_low_importance)
            NORMAL -> context.resources.getString(R.string.txt_normal_importance)
            HIGH -> context.resources.getString(R.string.txt_high_importance)
        }
    }

    companion object {
        fun fromServerImportance(importance: String): TaskImportance {
            return when (importance) {
                "low" -> LOW
                "basic" -> NORMAL
                "important" -> HIGH
                else -> throw IllegalArgumentException("Unknown importance level: $importance")
            }
        }

        fun toServerImportance(importance: TaskImportance): String {
            return when (importance) {
                LOW -> "low"
                NORMAL -> "basic"
                HIGH -> "important"
            }
        }
    }
}