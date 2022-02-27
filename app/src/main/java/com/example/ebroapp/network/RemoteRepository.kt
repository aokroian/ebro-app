package com.example.ebroapp.network

import com.example.ebroapp.models.CurrentWeather
import com.example.ebroapp.models.FullWeather
import io.reactivex.Single

interface RemoteRepository {

    companion object {
        @Volatile
        private var instance: RemoteRepository? = null

        fun obtain(): RemoteRepository {
            instance = instance ?: synchronized(this) {
                RemoteRepositoryImpl()
            }
            return instance!!
        }
    }

    fun getCurrentWeather(): Single<CurrentWeather>

    fun getWeatherFull(): Single<FullWeather>
}