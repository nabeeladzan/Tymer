package org.finalware.tymer.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.finalware.tymer.model.Bin
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://avatars.githubusercontent.com/u/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ProfileService {
    @GET("63929296?v=4")
    suspend fun getProfileImage(): Bin
}

object ProfileApi {
    val service: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}