package tech.movies.app.presentation.nav

sealed class ScreenRoutes(val route: String) {
    data object Home : ScreenRoutes("home")
    data object Search : ScreenRoutes("search")
    data object Detail : ScreenRoutes("detail/{movie_id}")
}