package com.dijkstra.pathfinder.di

import com.dijkstra.pathfinder.domain.api.TestApi
import com.dijkstra.pathfinder.util.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
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

    @Provides
    @Singleton
    fun testCallApi(): TestApi {
        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    } // End of testCallApi


} // End of AppModule
