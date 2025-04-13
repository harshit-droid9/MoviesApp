package tech.movies.app.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import tech.movies.app.common.UiState
import tech.movies.app.domain.repository.Repository
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    repository: Repository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val movieId: Int = savedStateHandle.get<Int>("movie_id")
        ?: throw IllegalStateException("movie id was not passed with args")

    private val currentState = MutableStateFlow(DetailScreenState())
    private val _state = MutableSharedFlow<UiState<DetailScreenState>>(1)
    val state: StateFlow<UiState<DetailScreenState>> = merge(
        repository.getMovieById(movieId)
            .map { movie ->
                updateState(currentState.value.copy(movie = movie))
            }
            .onStart { emit(UiState.Loading) }
            .catch { e -> emit(UiState.Error(e.message ?: "Unknown error")) },
        _state
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState.Loading
    )

    private fun updateState(newState: DetailScreenState): UiState<DetailScreenState> {
        currentState.update { newState }
        return UiState.Success(currentState.value)
    }

}