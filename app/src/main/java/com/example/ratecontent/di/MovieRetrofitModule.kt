package com.example.ratecontent.di

import com.example.ratecontent.utils.MovieAPIConstants
import com.example.ratecontent.data.api.TMDbApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieRetrofitModule {

    val authInterceptor = Interceptor { chain ->
        val original: Request = chain.request()
        val requestBuilder = original.newBuilder()
            .header("Authorization", "Bearer ${MovieAPIConstants.v4Token}")
        val request = requestBuilder.build()
        chain.proceed(request)
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @MovieRetrofit
    @Singleton
    @Provides
    fun provideMovieRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MovieAPIConstants.BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieApiService(@MovieRetrofit retrofit: Retrofit): TMDbApiService {
        return retrofit.create(TMDbApiService::class.java)
    }
}