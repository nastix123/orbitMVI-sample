package com.example.orbitmvi_sample.presentation

sealed class ImagesListSideEffect {
    data class ShowToast(val message: String) : ImagesListSideEffect()
    data object ShowProgressBar : ImagesListSideEffect()
    data object HideProgressBar : ImagesListSideEffect()
    data class NavigateToErrorScreen(val errorMessage: String) : ImagesListSideEffect()
}