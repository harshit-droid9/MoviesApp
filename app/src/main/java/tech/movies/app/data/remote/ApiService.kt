package tech.movies.app.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import tech.movies.app.data.remote.model.GetTrendingMovieResponse

interface ApiService {

    @GET("trending/movie/week")
    suspend fun getTrendingMoviesForWeek(
        @Query("api_key") apiKey: String
    ): GetTrendingMovieResponse
}