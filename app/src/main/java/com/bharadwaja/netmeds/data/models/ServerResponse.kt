package com.bharadwaja.netmeds.data.models

sealed class ServerResponse<out T : Any> {

    data class Success<out T : Any>(val data: T) : ServerResponse<T>()
    data class Error(val exception: Exception) : ServerResponse<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}