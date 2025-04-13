package tech.movies.app.presentation.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tech.movies.app.common.UiState
import tech.movies.app.domain.model.Movie
import tech.movies.app.presentation.home.MovieGrid
import tech.movies.app.presentation.home.SearchBar

data class SearchScreenState(
    val query: String = "",
    val results: List<Movie> = emptyList(),
)

@Composable
fun SearchScreen(
    onMovieClicked: (Int) -> Unit
) {
    val viewModel: SearchViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { internalPadding ->
        SearchScreenContent(
            state = state,
            onQueryChanges = { viewModel.onQueryChanged(it) },
            onMovieClicked = onMovieClicked,
            modifier = Modifier
                .fillMaxSize()
                .padding(internalPadding)
        )
    }
}

@Composable
fun SearchScreenContent(
    modifier: Modifier = Modifier,
    state: UiState<SearchScreenState>,
    onQueryChanges: (String) -> Unit,
    onMovieClicked: (Int) -> Unit
) {
    when (state) {
        is UiState.Loading -> Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator() }

        is UiState.Success -> {
            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(Unit) { focusRequester.requestFocus() }

            Column(modifier = modifier) {
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .focusRequester(focusRequester),
                    value = state.data.query,
                    onValueChange = onQueryChanges,
                    isEnabled = true
                )

                MovieGrid(
                    movies = state.data.results,
                    onMovieClicked = onMovieClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
        }

        is UiState.Error -> Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) { Text(state.errorMessage) }
    }
}

