package com.example.orbitmvi_sample.domain.repo

import com.example.orbitmvi_sample.domain.model.Image
import kotlinx.coroutines.flow.Flow

interface ApiRepository {

    suspend fun getPhotos(): Flow<List<Image>>

    suspend fun getPhoto(photoId: Int): Image
}