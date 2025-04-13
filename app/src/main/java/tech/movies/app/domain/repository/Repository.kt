package tech.movies.app.domain.repository

import kotlinx.coroutines.flow.Flow
import tech.movies.app.domain.model.Movie

interface Repository {
    fun fetchTrendingMovies(): Flow<List<Movie>>

    fun getMovieById(movieId: Int): Flow<Movie>

    suspend fun searchMovies(query: String): List<Movie>
}