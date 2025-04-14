package tech.movies.app.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<S>(
    initial: S,
    showLoading: Boolean,
) : ViewModel() {

    protected val currentState = MutableStateFlow(initial)
    private val _uiState = MutableStateFlow<UiState<S>>(UiState.Success(initial))

    /** Exposed state for ui screen to consume it */
    val uiState: StateFlow<UiState<S>> = channelFlow {

        launch {
            _uiState
                .drop(1) // we already sending with initial value
                .collect { send(it) }
        }

        val loadInitial = try {
            initialLoad()
        } catch (e: Exception) {
            send(UiState.Error(e.message ?: "Unknown error"))
            null
        }

        loadInitial?.let { flow ->
            launch {
                send(UiState.Loading)
                flow
                    .catch { e ->
                        send(UiState.Error(e.message ?: "Unknown error"))
                    }
                    .collect { newState ->
                        setState { newState }
                        send(UiState.Success(newState))
                    }
            }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = if (showLoading) UiState.Loading else UiState.Success(initial)
        )

    /** Use for loading initial data, something we wanted to load on start of the screen*/
    protected open fun initialLoad(): Flow<S>? = null

    protected fun setState(reducer: S.() -> S) {
        currentState.update { currentState.value.reducer() }
        _uiState.tryEmit(UiState.Success(currentState.value))
    }
}
