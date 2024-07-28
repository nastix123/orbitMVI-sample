package com.example.orbitmvi_sample.data.model

import com.google.gson.annotations.SerializedName

data class PhotosResponse(
    @SerializedName("photos")
    val listOfPhotos: List<PhotoDto>
)
