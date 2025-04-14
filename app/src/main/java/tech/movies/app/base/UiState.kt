package tech.movies.app.base

/**
 * A simple, one–shot wrapper that represents **exactly one** of three UI‑rendering
 * states for a screen or component.
 *
 *```
 * UiState<T>
 *  ├── Loading          // the data is being fetched
 *  ├── Success<T>       // the data is ready
 *  └── Error            // something went wrong
 *```
 */
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<out T>(val data: T) : UiState<T>
    data class Error(val errorMessage: String) : UiState<Nothing>
}
