package com.example.orbitmvi_sample.presentation.list

import com.example.orbitmvi_sample.domain.model.Photo

data class ImagesListState(
    val images: List<Photo> = emptyList(),
    val selectedPhoto: Photo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
