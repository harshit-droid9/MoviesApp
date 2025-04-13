package tech.movies.app.presentation.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import tech.movies.app.presentation.detail.DetailScreen
import tech.movies.app.presentation.home.HomeScreen
import tech.movies.app.presentation.search.SearchScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController, startDestination = ScreenRoutes.Home.route) {

        composable(
            route = ScreenRoutes.Home.route
        ) {
            HomeScreen(
                onMovieClick = {
                    navController.navigate(
                        ScreenRoutes.Detail.route.replace("{movie_id}", it.toString())
                    )
                },
                onSearchBarClick = {
                    navController.navigate(ScreenRoutes.Search.route)
                }
            )
        }

        composable(
            route = ScreenRoutes.Detail.route,
            arguments = listOf(navArgument("movie_id") { type = NavType.IntType })
        ) { backStackEntry ->
            DetailScreen(
                movieId = backStackEntry.arguments?.getInt("movie_id")
                    ?: throw IllegalArgumentException("Movie id is missing"),
                onBackPress = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = ScreenRoutes.Search.route
        ) {
            SearchScreen(
                onBackPress = {
                    navController.popBackStack()
                }
            )
        }
    }
}