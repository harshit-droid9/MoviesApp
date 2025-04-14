package tech.movies.app.presentation.home

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tech.movies.app.base.BaseViewModel
import tech.movies.app.domain.repository.Repository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : BaseViewModel<HomeScreenState>(HomeScreenState(), showLoading = true) {

    override fun initialLoad(): Flow<HomeScreenState> =
        repository.fetchTrendingMovies()
            .map { list -> currentState.value.copy(movies = list) }
}