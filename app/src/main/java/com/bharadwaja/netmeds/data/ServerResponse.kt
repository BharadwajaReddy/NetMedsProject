package com.bharadwaja.netmeds.data

sealed class ServerResponse<out T : Any> {

    data class Success<out T : Any>(val data: T) : ServerResponse<T>()
    data class Error(val exception: Exception) : ServerResponse<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Successfully fetched data [data=$data]"
            is Error -> "Error while fetching the data [exception=$exception]"
        }
    }
}