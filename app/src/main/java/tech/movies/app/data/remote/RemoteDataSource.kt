package tech.movies.app.data.remote

import tech.movies.app.data.remote.model.ApiMovie
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    @ApiKey private val apiKey: String,
) {

    suspend fun getTrendingMovieForWeek(): List<ApiMovie> {
        return apiService.getTrendingMoviesForWeek(apiKey).results
    }
}