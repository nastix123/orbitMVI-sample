package com.example.orbitmvi_sample.data.repo

import android.util.Log
import com.example.orbitmvi_sample.data.model.PhotoDto
import com.example.orbitmvi_sample.domain.model.Photo
import com.example.orbitmvi_sample.domain.repo.ApiRepository
import com.example.orbitmvi_sample.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.koin.core.component.KoinComponent

class ApiRepositoryImpl(
    private val apiService: ApiService
): ApiRepository {
    override suspend fun getPhotos(): Flow<List<Photo>> {
        return flow {
            try {
                val response = apiService.getPhotos()
                if (response.isSuccessful) {
                    val photosResponse = response.body()
                    val photoList = photosResponse?.listOfPhotos?.map { it.toPhoto() } ?: emptyList()
                    Log.d("ApiRepositoryImpl", "new images were added")
                    emit(photoList)
                } else {
                    Log.d("ApiRepositoryImpl", "empty")
                    emit(emptyList())
                }
            } catch (e: Exception) {
                emit(emptyList())
            }
        }.flowOn(Dispatchers.IO)
    }


    override suspend fun getPhoto(photoId: Int): Photo {
        return try {
            val response = apiService.getPhoto(photoId)
            if (response.isSuccessful) {
                response.body()?.toPhoto() ?: Photo(0, "", "")
            } else {
                Photo(0, "", "")
            }
        } catch (e: Exception) {
            Photo(0, "", "")
        }
    }

}

fun PhotoDto.toPhoto(): Photo {
    return Photo(
        photoId = photoId,
        photoUrl = photoUrl,
        photographer = photographer
    )
}