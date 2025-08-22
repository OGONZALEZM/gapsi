package com.ogonzalezm.testgepsi.data.remote

import android.util.Log
import retrofit2.Response

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String): Resource<Nothing>()
}

suspend fun <T> safeApiCall(call: suspend () -> Response<T>): Resource<T> {
    return try {
        val response = call()
        if (response.isSuccessful) {
            Log.i("Resource", "Success")
            response.body()?.let { Resource.Success(it) }
                ?: Resource.Error("Empty response body")
        } else {
            Log.e("Resource", "Error1")
            Resource.Error(response.errorBody()?.string() ?: "Unknown error")
        }
    } catch (e: Exception) {
        Log.e("Resource", "Error ${e.message}")
        Resource.Error(e.localizedMessage ?: "Unknown exception")
    }
}