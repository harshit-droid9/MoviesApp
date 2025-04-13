package tech.movies.app.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface MoviesDao {
    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<MovieEntity>>

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