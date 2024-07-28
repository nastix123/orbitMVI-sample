package com.example.orbitmvi_sample.presentation.details

import com.example.orbitmvi_sample.domain.model.Photo

data class PhotoDetailState(
    val photo: Photo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
