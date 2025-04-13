package tech.movies.app.data.respositoryImpl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    // todo, while on Ui, remember to save recomposition when refresh trigger
    override suspend fun fetchTrendingMovies(): Flow<List<Movie>> {
        return withContext(Dispatchers.IO) {
            val age = System.currentTimeMillis() - localDataSource.getLastSyncTime()
            if (age > 60 * 60_000) {
                refreshTrendingMovies()
            }
            localDataSource.getTrendingMovies().map { it.toMovies() }
        }
    }

    private suspend fun refreshTrendingMovies() {
        val data = remoteDataSource.getTrendingMovieForWeek().toDbEntities()
        localDataSource.refreshMovies(data)
        localDataSource.saveLastSyncTime(epochMillis = System.currentTimeMillis())
    }
}