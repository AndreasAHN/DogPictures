package com.ahn.dogpictures.interfaces

import com.ahn.dogpictures.models.DogImageModel
import retrofit2.Response
import retrofit2.http.GET


//REF : Co-Routines with rest-api example : https://dev.to/rtficial/kotlin-coroutines-and-retrofit-a-practical-approach-to-consuming-rest-apis-in-android-446k
interface DogApiClient
{
    @GET("/api/breeds/image/random")
    suspend fun getRandomDogImage(): Response<DogImageModel>
}