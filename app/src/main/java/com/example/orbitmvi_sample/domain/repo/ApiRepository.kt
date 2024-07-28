package com.example.orbitmvi_sample.domain.repo

import com.example.orbitmvi_sample.domain.model.Photo
import kotlinx.coroutines.flow.Flow

interface ApiRepository {

    suspend fun getPhotos(): Flow<List<Photo>>

    suspend fun getPhoto(photoId: Int): Photo
}