package com.dijkstra.pathfinder

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.viewModelFactory
import com.dijkstra.pathfinder.util.Constant
import com.dijkstra.pathfinder.util.SharedPreferencesUtil
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.hilt.android.HiltAndroidApp
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import okio.IOException
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        initRetrofit(AppInterceptor())
        sharedPreferenceUtil = SharedPreferencesUtil(applicationContext)
    }

    private fun initRetrofit(interceptor: AppInterceptor) {
        val logging = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            logging.level = HttpLoggingInterceptor.Level.BODY
        } else {
            logging.level = HttpLoggingInterceptor.Level.NONE
        }

        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addNetworkInterceptor(HttpLoggingInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(1, TimeUnit.MINUTES)
            .build()

        val gson: Gson = GsonBuilder() //날짜 데이터 포맷
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create()

        retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(okHttpClient)
            .build()

        val headerOkHttpClient = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addNetworkInterceptor(HttpLoggingInterceptor())
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(interceptor)
            .build()

        headerRetrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(headerOkHttpClient)
            .build()
    }

    inner class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val newRequest = request().newBuilder()
                .build()
            proceed(newRequest)
        }
    } // End of AppInterceptor inner class

    companion object {
        lateinit var retrofit: Retrofit
        lateinit var headerRetrofit: Retrofit
        lateinit var sharedPreferenceUtil: SharedPreferencesUtil
    }

} // End of Application class