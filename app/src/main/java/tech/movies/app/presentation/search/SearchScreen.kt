package tech.movies.app.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import tech.movies.app.R
import tech.movies.app.base.UiState
import tech.movies.app.domain.model.Movie
import tech.movies.app.presentation.home.MovieGrid
import tech.movies.app.presentation.home.SearchBar
import tech.movies.app.presentation.util.UiStateHandler

data class SearchScreenState(
    val query: String = "",
    val results: List<Movie> = emptyList(),
)

@Composable
fun SearchScreen(
    onMovieClicked: (Int) -> Unit
) {
    val viewModel: SearchViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { internalPadding ->
        SearchScreenContent(
            uiState = state,
            onQueryChanged = { viewModel.onQueryChanged(it) },
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
    uiState: UiState<SearchScreenState>,
    onQueryChanged: (String) -> Unit,
    onMovieClicked: (Int) -> Unit
) {
    UiStateHandler(
        uiState = uiState,
        modifier = modifier,
        success = { state ->
            val focusRequester = remember { FocusRequester() }
            LaunchedEffect(Unit) { focusRequester.requestFocus() }

            var searchQuery by rememberSaveable(stateSaver = TextFieldValue.Saver) {
                mutableStateOf(TextFieldValue(state.query))
            }

            Column(modifier = modifier) {
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .focusRequester(focusRequester),
                    value = searchQuery,
                    onValueChange = { newValue ->
                        searchQuery = newValue
                        onQueryChanged(searchQuery.text)
                    },
                    isEnabled = true
                )

                if (state.results.isNotEmpty()) {
                    MovieGrid(
                        movies = state.results,
                        onMovieClicked = onMovieClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                } else {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.no_result_found),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    )
}

