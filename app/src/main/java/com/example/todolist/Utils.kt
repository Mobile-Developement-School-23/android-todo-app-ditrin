package com.example.todolist

import com.example.todolist.domain.Importance
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toDateString(dateFormat: Int = DateFormat.MEDIUM): String {
    val df = DateFormat.getDateInstance(dateFormat, Locale.getDefault())
    return df.format(this)
}

fun Long?.parseDate(): String {
    val date = Date((this ?: 0))
    val format = SimpleDateFormat("d MMM yyyy", Locale("ru"))
    return format.format(date)
}

fun Importance.chooseLvl(): String {
    return when (this) {
        Importance.LOW -> {
            "Низкий"
        }

        Importance.COMMON -> {
            "Нет"
        }

        Importance.HIGH -> {
            "!! Высокий"
        }
    }
}