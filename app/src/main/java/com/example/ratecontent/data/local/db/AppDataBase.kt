package com.example.ratecontent.data.local.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ratecontent.data.local.entities.FavoriteBook
import com.example.ratecontent.data.local.entities.FavoriteMovie
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.ratecontent.data.local.entities.FavoriteGame
import com.example.ratecontent.data.local.utility.Converters


@Database(entities = [FavoriteMovie::class, FavoriteBook::class, FavoriteGame::class], version = 7)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteMovieDao(): FavoriteMovieDao
    abstract fun favoriteBookDao(): FavoriteBookDao
    abstract fun favoriteGameDao(): FavoriteGameDao
}