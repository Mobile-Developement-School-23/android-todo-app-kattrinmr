package com.katerina.todoapp.data.api.exceptions

import java.io.IOException

internal class NoConnectivityException : IOException() {
    override val message: String
        get() = "No network available. Please, check your internet connection!"
}