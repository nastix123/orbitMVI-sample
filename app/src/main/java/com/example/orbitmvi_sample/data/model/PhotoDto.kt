package com.example.orbitmvi_sample.data.model

import com.google.gson.annotations.SerializedName

data class PhotoDto(
    @SerializedName("id")
    val photoId: Int,
    @SerializedName("url")
    val photoUrl: String,
    @SerializedName("photographer")
    val photographer: String
)
