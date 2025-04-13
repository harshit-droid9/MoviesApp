package tech.movies.app.presentation.nav

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
    NavHost(
        navController = navController,
        startDestination = ScreenRoutes.Home.route
    ) {

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
            arguments = listOf(navArgument("movie_id") { type = NavType.IntType }),
            enterTransition = {
                slideInHorizontally(
                    animationSpec = tween(300),
                    initialOffsetX = { it }
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { it }
                )
            }
        ) {
            DetailScreen(
                onBackPress = {
                    navController.navigateUp()
                }
            )
        }

        composable(
            route = ScreenRoutes.Search.route
        ) {
            SearchScreen(
                onBackPress = {
                    navController.navigateUp()
                }
            )
        }
    }
}