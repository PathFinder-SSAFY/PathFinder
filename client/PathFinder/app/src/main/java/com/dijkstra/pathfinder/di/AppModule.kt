package com.dijkstra.pathfinder.di

import androidx.core.view.DragAndDropPermissionsCompat.*
import com.dijkstra.pathfinder.BuildConfig
import com.dijkstra.pathfinder.domain.api.MainApi
import com.dijkstra.pathfinder.domain.api.NFCApi
import com.dijkstra.pathfinder.domain.api.TestApi
import com.dijkstra.pathfinder.util.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OkHttpInterceptorClient

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OkHttpInterceptorApi

    class OkHttpInterceptor @Inject constructor() : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .build()
            proceed(newRequest)
        }
    } // End of okHttpInterceptor class

    @Provides
    @OkHttpInterceptorClient
    fun provideInterceptorOkHttpClient(okHttpInterceptor: OkHttpInterceptor): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.NONE
        }

        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addNetworkInterceptor(HttpLoggingInterceptor())
            .addInterceptor(okHttpInterceptor)
            .connectTimeout(1, TimeUnit.MINUTES)
            .build()
    } // End of provideInterceptorOkHttpClient

    // ============================================ Test Retrofit Module ============================================
    @Provides
    @OkHttpInterceptorApi
    @Singleton
    fun testCallApi(@OkHttpInterceptorClient interceptor: OkHttpClient): TestApi {
        return Retrofit.Builder()
            .client(interceptor)
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    } // End of testCallApi

    // ============================================ Main Retrofit Module ============================================
    @Provides
    @OkHttpInterceptorApi
    @Singleton
    fun mainCallApi(@OkHttpInterceptorClient interceptor: OkHttpClient): MainApi {
        return Retrofit.Builder()
            .client(interceptor)
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    } // End of mainCallApi

    // ============================================ NFC Retrofit Module ============================================
    @Provides
    @OkHttpInterceptorApi
    @Singleton
    fun nfcCallApi(@OkHttpInterceptorClient interceptor: OkHttpClient): NFCApi {
        return Retrofit.Builder()
            .client(interceptor)
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    } // End of nfcCallApi
} // End of AppModule object
