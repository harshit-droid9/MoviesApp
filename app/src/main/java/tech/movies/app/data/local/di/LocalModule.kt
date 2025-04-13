package tech.movies.app.data.local.di

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import tech.movies.app.data.local.database.MovieDatabase
import tech.movies.app.data.local.database.MoviesDao
import tech.movies.app.data.local.pref.AppPreferences
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    private const val PREF_FILE = "movies_prefs"

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): MovieDatabase =
        Room
            .databaseBuilder(ctx, MovieDatabase::class.java, "movies.db")
            .build()

    @Provides
    fun provideMovieDao(db: MovieDatabase): MoviesDao = db.moviesDao()

    @Provides
    @Singleton
    fun provideSharedPrefs(@ApplicationContext ctx: Context): SharedPreferences =
        ctx.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideSyncPrefs(prefs: android.content.SharedPreferences): AppPreferences =
        AppPreferences(prefs)
}