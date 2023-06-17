package org.finalware.tymer.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.finalware.tymer.model.Bin
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.jsonbin.io/v3/b/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ApiService {
    @GET("648d5ab58e4aa6225eafa984")
    suspend fun getPresetsBin(): Bin
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }

object Api {
    val service: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}