package tech.movies.app.data.local.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import tech.movies.app.data.local.database.MovieDatabase
import tech.movies.app.data.local.database.MoviesDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): MovieDatabase =
        Room
            .databaseBuilder(ctx, MovieDatabase::class.java, "movies.db")
            .build()

    @Provides
    fun provideMovieDao(db: MovieDatabase): MoviesDao = db.moviesDao()
}