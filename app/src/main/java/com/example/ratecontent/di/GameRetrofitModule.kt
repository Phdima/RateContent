package com.example.ratecontent.di

import com.example.ratecontent.data.api.GamesAPIService
import com.example.ratecontent.utils.GamesAPIConstants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object GameRetrofitModule {
    private val client = OkHttpClient.Builder()
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @GameRetrofit
    @Singleton
    @Provides
    fun providesGameRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GamesAPIConstants.GAMES_BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Singleton
    @Provides
    fun provideGameApiService(@GameRetrofit retrofit: Retrofit): GamesAPIService{
        return retrofit.create(GamesAPIService::class.java)
    }
}