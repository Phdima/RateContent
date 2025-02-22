package com.example.ratecontent.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class MovieRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BookRetrofit
