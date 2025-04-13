package tech.movies.app.domain.model

import tech.movies.app.data.local.database.MovieEntity

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
)

fun MovieEntity.toMovie() = Movie(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath
)

fun List<MovieEntity>.toMovies() = this.map { it.toMovie() }