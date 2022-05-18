package com.ahn.dogpictures.models

import androidx.annotation.Keep

//REF : Co-Routines with rest-api example : https://dev.to/rtficial/kotlin-coroutines-and-retrofit-a-practical-approach-to-consuming-rest-apis-in-android-446k
@Keep
public class DogImageModel
{
    var message: String? = null
    var status: String? = null
}