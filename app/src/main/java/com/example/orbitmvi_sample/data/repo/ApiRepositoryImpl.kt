package com.example.orbitmvi_sample.data.repo

import android.util.Log
import com.example.orbitmvi_sample.data.model.PhotoDto
import com.example.orbitmvi_sample.domain.model.Image
import com.example.orbitmvi_sample.domain.repo.ApiRepository
import com.example.orbitmvi_sample.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ApiRepositoryImpl(
    private val apiService: ApiService
): ApiRepository {
    override suspend fun getPhotos(): Flow<List<Image>> {
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


    override suspend fun getPhoto(photoId: Int): Image {
        return try {
            val response = apiService.getPhoto(photoId)
            if (response.isSuccessful) {
                response.body()?.toPhoto() ?: Image(0, "", "")
            } else {
                Image(0, "", "")
            }
        } catch (e: Exception) {
            Image(0, "", "")
        }
    }

}

fun PhotoDto.toPhoto(): Image {
    return Image(
        id = photoId,
        url = src.original,
        photographer = photographer
    )
}