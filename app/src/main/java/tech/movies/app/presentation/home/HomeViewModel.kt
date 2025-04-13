package tech.movies.app.presentation.home

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
class HomeViewModel @Inject constructor(
    repository: Repository
) : ViewModel() {

    private var currentState = MutableStateFlow(HomeScreenState())
    private val _state = MutableSharedFlow<UiState<HomeScreenState>>(1)
    val state: StateFlow<UiState<HomeScreenState>> =
        merge(
            repository.fetchTrendingMovies().map { list ->
                updateState(currentState.value.copy(movies = list))
            }
                .onStart { emit(UiState.Loading) }
                .catch { e -> emit(UiState.Error(e.message ?: "Unknown error")) }, _state
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )

    private fun updateState(newState: HomeScreenState): UiState<HomeScreenState> {
        currentState.update { newState }
        return UiState.Success(currentState.value)
    }
}