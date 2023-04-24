package com.dijkstra.pathfinder.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /* Dagger-Hilt Example Code
    @Provides
    fun provideBaseUrl() = BASE_URL


    @Singleton
    @Provides
    fun provideExampleApi() : ExampleApi {
        return Retrofit.Builder()
            .baseUrl(provideBaseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExampleApi::class.java)
    }
    */

}