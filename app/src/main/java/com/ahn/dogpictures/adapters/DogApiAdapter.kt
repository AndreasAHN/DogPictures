package com.ahn.dogpictures.adapters

import com.ahn.dogpictures.interfaces.DogApiClient
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//REF : Co-Routines with rest-api example : https://dev.to/rtficial/kotlin-coroutines-and-retrofit-a-practical-approach-to-consuming-rest-apis-in-android-446k
object DogApiAdapter
{
    val dogApiClient: DogApiClient = Retrofit.Builder()
        .baseUrl("https://dog.ceo")
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DogApiClient::class.java)
}