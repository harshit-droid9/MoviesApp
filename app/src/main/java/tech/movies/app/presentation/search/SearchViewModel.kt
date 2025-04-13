package tech.movies.app.presentation.search

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

import kotlinx.coroutines.launch
import tech.movies.app.common.BaseViewModel
import tech.movies.app.domain.repository.Repository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: Repository
) : BaseViewModel<SearchScreenState>(SearchScreenState(), isLoading = false) {

    private var searchJob: Job? = null
    fun onQueryChanged(newQuery: String) {
        setState { copy(query = newQuery) }

        if (newQuery.isBlank()) {
            setState { copy(results = emptyList()) }
            return
        }
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(300)
            val movies = repository.searchMovies(newQuery)
            setState { copy(results = movies) }
        }
    }
}