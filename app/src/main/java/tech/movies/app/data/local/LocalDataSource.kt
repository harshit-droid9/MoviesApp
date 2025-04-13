package tech.movies.app.data.local

import tech.movies.app.data.local.database.MovieEntity
import tech.movies.app.data.local.database.MoviesDao
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val moviesDao: MoviesDao
) {

    suspend fun getTrendingMovies(): List<MovieEntity> {
        return moviesDao.getAllMovies()
    }

    suspend fun insertMovies(movies: List<MovieEntity>) {
        return moviesDao.insert(movies)
    }

    suspend fun refreshMovies(movies: List<MovieEntity>) {
        return moviesDao.refreshMoviesData(movies)
    }
}