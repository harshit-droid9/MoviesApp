package tech.movies.app.data.remote.model

import com.google.gson.annotations.SerializedName

data class GetTrendingMovieResponse(
    val page: Int,
    val results: List<ApiMovie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)

data class ApiMovie(
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    val id: Int,
    val title: String,
    @SerializedName("original_title")
    val originalTitle: String,
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("media_type")
    val mediaType: String,
    val adult: Boolean,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    val popularity: Double,
    @SerializedName("release_date")
    val releaseDate: String,
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
)