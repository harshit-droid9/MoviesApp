package tech.movies.app.data.mapper

import tech.movies.app.data.local.database.MovieEntity
import tech.movies.app.data.remote.model.ApiMovie

fun ApiMovie.toEntity(): MovieEntity = MovieEntity(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
)

fun List<ApiMovie>.toDbEntities() = this.map { it.toEntity() }