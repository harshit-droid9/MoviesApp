package tech.movies.app.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.movies.app.common.UiState
import tech.movies.app.domain.repository.Repository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {


    private val currentState = MutableStateFlow(SearchScreenState())
    private val _state = MutableSharedFlow<UiState<SearchScreenState>>(replay = 1)
    val state: StateFlow<UiState<SearchScreenState>> = merge(
        flow { emit(UiState.Success(SearchScreenState())) },
        _state
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiState.Success(SearchScreenState())
    )

    private fun updateState(newState: SearchScreenState): UiState<SearchScreenState> {
        currentState.update { newState }
        return UiState.Success(newState)
    }

    fun onQueryChanged(newQuery: String) {
        viewModelScope.launch {
            val updatedResults = repository.searchMovies(newQuery)
            val newState = SearchScreenState(
                query = newQuery,
                results = updatedResults
            )

            _state.emit(updateState(newState))
        }
    }
}