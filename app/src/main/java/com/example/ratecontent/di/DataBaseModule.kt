package com.example.ratecontent.di

import android.content.Context
import androidx.room.Room
import com.example.ratecontent.data.local.db.AppDatabase
import com.example.ratecontent.data.local.db.FavoriteMovieDao
import com.example.ratecontent.data.local.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideFavoriteMovieDao(database: AppDatabase): FavoriteMovieDao {
        return database.favoriteMovieDao()
    }

    @Provides
    @Singleton
    fun provideMovieRepository(dao: FavoriteMovieDao): MovieRepository {
        return MovieRepository(dao)
    }
}