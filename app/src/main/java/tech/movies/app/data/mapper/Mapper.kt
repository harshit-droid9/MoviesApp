package tech.movies.app.data.mapper

import tech.movies.app.data.local.database.MovieEntity
import tech.movies.app.data.remote.model.ApiMovie

private const val BaseUrl = "https://image.tmdb.org/t/p/w500"

fun ApiMovie.toEntity(): MovieEntity = MovieEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = BaseUrl + posterPath,
    backdropPath = BaseUrl + backdropPath,
)

fun List<ApiMovie>.toDbEntities() = this.map { it.toEntity() }