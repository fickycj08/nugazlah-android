package top.nabil.nugazlah

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import top.nabil.nugazlah.data.ClassCardState
import top.nabil.nugazlah.screen.AddTaskScreen
import top.nabil.nugazlah.screen.ClassScreen
import top.nabil.nugazlah.screen.DetailTaskScreen
import top.nabil.nugazlah.screen.HomeScreen
import top.nabil.nugazlah.screen.LoginScreen
import top.nabil.nugazlah.screen.Screen
import top.nabil.nugazlah.ui.theme.NugazlahTheme
import top.nabil.nugazlah.ui.theme.DarkSurface

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            NugazlahTheme(
                darkTheme = true,
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkSurface
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.LoginScreen
                    ) {
                        composable(route = Screen.LoginScreen) {
                            LoginScreen(navController = navController)
                        }
                        composable(route = Screen.HomeScreen) {
                            HomeScreen(navController = navController)
                        }
                        composable(route = Screen.ClassScreen) {
                            ClassScreen(navController = navController)
                        }
                        composable(route = Screen.DetailTaskScreen) {
                            DetailTaskScreen(navController = navController)
                        }
                        composable(route = Screen.AddTaskScreen) {
                            AddTaskScreen(navController = navController)
                        }
                    }
                }
            }
        }
    }
}
