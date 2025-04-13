package tech.movies.app.data.local.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

interface MoviesDao {
    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movies: List<MovieEntity>)

    @Query("DELETE FROM movies")
    suspend fun deleteAllEntries()

    @Transaction
    suspend fun refreshMoviesData(
        movies: List<MovieEntity>
    ) {
        deleteAllEntries()
        insert(movies)
    }
}