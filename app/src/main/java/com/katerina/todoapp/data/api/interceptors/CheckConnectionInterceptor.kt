package com.katerina.todoapp.data.api.interceptors

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.katerina.todoapp.data.api.exceptions.NoConnectivityException
import com.katerina.todoapp.data.utils.AUTH_TOKEN
import okhttp3.Interceptor
import okhttp3.Response

class CheckConnectionInterceptor(
    private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return if (!isConnected()) { throw NoConnectivityException() }
        else {
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .header("Authorization", AUTH_TOKEN)
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    private fun isConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = cm.activeNetwork
        val connection = cm.getNetworkCapabilities(network)

        return connection != null && (
                connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }
}