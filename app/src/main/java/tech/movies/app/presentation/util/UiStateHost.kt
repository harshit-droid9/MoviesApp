package tech.movies.app.presentation.util

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import tech.movies.app.base.UiState

@Composable
fun <T> UiStateHandler(
    uiState: UiState<T>,
    modifier: Modifier = Modifier,
    loading: @Composable () -> Unit = {
        Box(modifier, Alignment.Center) { CircularProgressIndicator() }
    },
    error: @Composable (String) -> Unit = { msg ->
        Box(modifier, Alignment.Center) { Text(msg) }
    },
    success: @Composable (T) -> Unit
) {
    when (uiState) {
        is UiState.Loading -> loading()
        is UiState.Success -> success(uiState.data)
        is UiState.Error -> error(uiState.errorMessage)
    }
}