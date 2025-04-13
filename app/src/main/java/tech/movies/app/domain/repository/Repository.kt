package tech.movies.app.domain.repository

import kotlinx.coroutines.flow.Flow
import tech.movies.app.domain.model.Movie

interface Repository {
    suspend fun fetchTrendingMovies(): Flow<List<Movie>>
}