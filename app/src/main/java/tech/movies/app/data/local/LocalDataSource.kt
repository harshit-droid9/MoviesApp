package tech.movies.app.data.local

import kotlinx.coroutines.flow.Flow
import tech.movies.app.data.local.database.MovieEntity
import tech.movies.app.data.local.database.MoviesDao
import tech.movies.app.data.local.pref.AppPreferences
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val moviesDao: MoviesDao,
    private val pref: AppPreferences
) {

    suspend fun getTrendingMovies(): Flow<List<MovieEntity>> {
        return moviesDao.getAllMovies()
    }

    suspend fun insertMovies(movies: List<MovieEntity>) {
        return moviesDao.insert(movies)
    }

    suspend fun refreshMovies(movies: List<MovieEntity>) {
        return moviesDao.refreshMoviesData(movies)
    }

    suspend fun getLastSyncTime(): Long = pref.lastSyncTime()

    suspend fun saveLastSyncTime(epochMillis: Long) = pref.saveLastSyncTime(epochMillis)
}