package com.example.ratecontent.di

import com.example.ratecontent.utils.BookAPIConstants
import com.example.ratecontent.data.api.BookApiService
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
object BookRetrofitModule {

    private val client = OkHttpClient.Builder()
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @BookRetrofit
    @Singleton
    @Provides
    fun providesBookRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BookAPIConstants.BOOK_BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Singleton
    @Provides
    fun provideBookApiService(@BookRetrofit retrofit: Retrofit): BookApiService {
        return retrofit.create(BookApiService::class.java)
    }
}

