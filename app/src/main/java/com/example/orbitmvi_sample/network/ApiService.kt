package com.example.orbitmvi_sample.network

import com.example.orbitmvi_sample.data.model.PhotoDto
import com.example.orbitmvi_sample.data.model.PhotosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ApiService {

    @GET("curated")
    suspend fun getPhotos(): Response<PhotosResponse>

    @GET("photos/{photoId}")
    suspend fun getPhoto(@Path("photoId") photoId: Int): Response<PhotoDto>

}

object Const {
    const val API_KEY = "fSIEuzHEyEAdGmmoxdLLaMJvz3yY9l6hLpeAkCKiV1dE1XrFw7aqm2SD"
    const val BASE_URL = "https://api.pexels.com/v1/"
    const val AUTH = "Authorization"
}