package com.bharadwaja.netmeds.utilities.networking

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface RetrofitInterface {


    @GET
    fun getSearchResponse(@Url fileUrl: String?): Call<ResponseBody?>?

    @Streaming
    @GET
    fun downloadFileByUrl(@Url fileUrl: String?): Call<ResponseBody?>?
}