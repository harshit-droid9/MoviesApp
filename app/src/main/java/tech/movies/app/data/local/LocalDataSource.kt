package tech.movies.app.data.local

import kotlinx.coroutines.flow.Flow
import tech.movies.app.data.local.database.MovieEntity
import tech.movies.app.data.local.database.MoviesDao
import tech.movies.app.data.local.pref.AppPreferences
import tech.movies.app.domain.model.Movie
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val moviesDao: MoviesDao,
    private val pref: AppPreferences
) {

    fun getTrendingMovies(): Flow<List<MovieEntity>> {
        return moviesDao.getAllMovies()
    }

    suspend fun refreshMovies(movies: List<MovieEntity>) {
        return moviesDao.refreshMoviesData(movies)
    }

    suspend fun getLastSyncTime(): Long = pref.lastSyncTime()

    suspend fun saveLastSyncTime(epochMillis: Long) = pref.saveLastSyncTime(epochMillis)

    fun getMovieById(movieId: Int): Flow<Movie> {
        return moviesDao.getMovieById(movieId)
    }

    suspend fun searchMovies(query: String): List<Movie> {
        return moviesDao.searchMovies(query)
    }
}