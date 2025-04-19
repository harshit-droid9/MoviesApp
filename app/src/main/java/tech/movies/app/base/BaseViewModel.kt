package tech.movies.app.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<S>(
    initial: S,
    showLoading: Boolean,
) : ViewModel() {

    /** Single source of truth for the plain screen state. */
    protected val currentState = MutableStateFlow(initial)

    /**
     * Stream collected by the UI layer.
     *
     * Cold until the first collector appears.
     * Re‑emits every update from [setState].
     * Runs [initialLoad] exactly once per active subscription set.
     */
    val uiState: StateFlow<UiState<S>> = channelFlow {

        launch {
            currentState
                .drop(1) // we already sending with initial value
                .collect { send(UiState.Success(it)) }
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

    /**
     * Override to provide a **cold** flow that loads the first page of data.
     *
     * - It is collected lazily—the flow does **not** start until `uiState`
     *   has at least one active collector.
     * * **Do not** register long‑lived listeners or open resources here.
     *   If you must, register a clean‑up block in `onCleared()`.
     *
     * Return `null` (default) if no start‑up fetch is needed.
     */
    protected open fun initialLoad(): Flow<S>? = null

    /**
     * Atomically transforms the current screen state.
     *
     * Thread‑safe—may be called from any dispatcher.
     *
     * @param reducer lambda that receives the **current** state and returns
     *                the **new** state.
     */
    protected fun setState(reducer: S.() -> S) {
        currentState.update { currentState.value.reducer() }
    }
}
