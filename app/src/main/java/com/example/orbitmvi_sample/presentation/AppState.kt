package com.example.orbitmvi_sample.presentation

import com.example.orbitmvi_sample.domain.model.Photo

data class ImagesListState(
    val images: List<Photo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
