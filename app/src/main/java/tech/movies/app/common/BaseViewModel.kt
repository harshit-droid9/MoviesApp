package tech.movies.app.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<S>(
    initial: S,
    isLoading: Boolean,
) : ViewModel() {

    protected val currentState = MutableStateFlow(initial)

    val uiState: StateFlow<UiState<S>> =
        channelFlow {
            if (isLoading) {
                send(UiState.Loading)
            } else {
                send(UiState.Success(currentState.value))
            }

            initialLoad()?.let { flow ->
                launch {
                    send(UiState.Loading)
                    flow
                        .catch { e ->
                            send(UiState.Error(e.message ?: "Unknown error"))
                        }
                        .collect { newState ->
                            currentState.value = newState
                            send(UiState.Success(newState))
                        }
                }
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = UiState.Success(initial)
            )


    protected open fun initialLoad(): Flow<S>? = null

    protected fun setState(reducer: S.() -> S) {
        currentState.value = currentState.value.reducer()
    }
}
