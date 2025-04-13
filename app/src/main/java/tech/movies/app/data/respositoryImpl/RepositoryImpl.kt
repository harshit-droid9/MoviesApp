package tech.movies.app.data.respositoryImpl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import tech.movies.app.data.local.LocalDataSource
import tech.movies.app.data.mapper.toDbEntities
import tech.movies.app.data.remote.RemoteDataSource
import tech.movies.app.domain.model.Movie
import tech.movies.app.domain.model.toMovies
import tech.movies.app.domain.repository.Repository
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : Repository {

    override fun fetchTrendingMovies(): Flow<List<Movie>> {
        return  localDataSource.getTrendingMovies().map { it.toMovies() }
            .onStart {
                val age = System.currentTimeMillis() - localDataSource.getLastSyncTime()
                if (age > 60 * 60_000) {
                    refreshTrendingMovies()
                }
            }
    }

    private suspend fun refreshTrendingMovies() {
        withContext(Dispatchers.IO) {
            val data = remoteDataSource.getTrendingMovieForWeek().toDbEntities()
            localDataSource.refreshMovies(data)
            localDataSource.saveLastSyncTime(epochMillis = System.currentTimeMillis())
        }
    }
}