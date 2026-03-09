package com.shrey.currencyx.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shrey.currencyx.ui.converter.ConverterScreen

sealed class Screen(val route: String) {
    data object Converter : Screen("converter")
}

@Composable
fun CurrencyNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Converter.route,
        modifier = modifier
    ) {
        composable(Screen.Converter.route) {
            ConverterScreen()
        }
    }
}
