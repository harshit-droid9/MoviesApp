package tech.movies.app.presentation.detail

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tech.movies.app.base.BaseViewModel
import tech.movies.app.domain.repository.Repository
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: Repository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<DetailScreenState>(DetailScreenState(), showLoading = true) {

    private val movieId: Int = savedStateHandle.get<Int>("movie_id")
        ?: throw IllegalStateException("movie id was not passed with args")

    override fun initialLoad(): Flow<DetailScreenState> = repository.getMovieById(movieId)
        .map { movie -> currentState.value.copy(movie = movie) }

}