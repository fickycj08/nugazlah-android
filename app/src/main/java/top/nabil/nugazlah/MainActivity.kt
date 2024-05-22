package top.nabil.nugazlah

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.runBlocking
import top.nabil.nugazlah.data.local.NugazlahDatabase
import top.nabil.nugazlah.data.remote.ServerConnect
import top.nabil.nugazlah.repository.AuthRepository
import top.nabil.nugazlah.repository.ClassRepository
import top.nabil.nugazlah.repository.TaskRepository
import top.nabil.nugazlah.screen.AddTaskScreen
import top.nabil.nugazlah.screen.AddTaskViewModel
import top.nabil.nugazlah.screen.ClassScreen
import top.nabil.nugazlah.screen.ClassViewModel
import top.nabil.nugazlah.screen.DetailTaskScreen
import top.nabil.nugazlah.screen.HomeScreen
import top.nabil.nugazlah.screen.HomeViewModel
import top.nabil.nugazlah.screen.LoginScreen
import top.nabil.nugazlah.screen.LoginViewModel
import top.nabil.nugazlah.screen.Screen
import top.nabil.nugazlah.screen.TaskDetailViewModel
import top.nabil.nugazlah.ui.theme.NugazlahTheme
import top.nabil.nugazlah.ui.theme.DarkSurface
import top.nabil.nugazlah.util.Resource
import top.nabil.nugazlah.util.vmFactoryHelper
import android.Manifest
import android.content.pm.PackageManager
import top.nabil.nugazlah.alarm.Scheduler

class MainActivity : ComponentActivity() {
    private lateinit var permissionsLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // TODO manage cancellation
        permissionsLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val manageExternal =
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            if (manageExternal != PackageManager.PERMISSION_GRANTED) {
                permissionsLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
            permissionsLauncher.launch(Manifest.permission.SCHEDULE_EXACT_ALARM)
        }

        setContent {
            val navController = rememberNavController()
            val scheduler by lazy { Scheduler(this) }
            val nugazlahDatabase by lazy { NugazlahDatabase.getInstance(this) }
            val authorizedNugazlahApi by lazy {
                val token = runBlocking {
                    nugazlahDatabase.tokenDao().get()
                }
                var tok = ""
                if (token != null) {
                    tok = token.token
                }
                ServerConnect.getAuthorizedInstance(tok)
            }

            val nugazlahApi by lazy { ServerConnect.getInstance() }
            val authRepository =
                AuthRepository(nugazlahApi, nugazlahDatabase.tokenDao())

            val startDestination = when (val result = runBlocking { authRepository.isLoggedIn() }) {
                is Resource.Success -> {
                    if (result.data!!) {
                        Screen.HomeScreen
                    } else {
                        Screen.LoginScreen
                    }
                }

                is Resource.Error -> {
                    Screen.LoginScreen
                }
            }

            NugazlahTheme(
                darkTheme = true,
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkSurface
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        composable(route = Screen.LoginScreen) {
                            val loginViewmodel = viewModel<LoginViewModel>(
                                factory = vmFactoryHelper {
                                    LoginViewModel(authRepository, navController)
                                }
                            )
                            LoginScreen(vm = loginViewmodel)
                        }
                        composable(route = Screen.HomeScreen) {
                            val classRepository =
                                ClassRepository(authorizedNugazlahApi)
                            val homeViewModel = viewModel<HomeViewModel>(
                                factory = vmFactoryHelper {
                                    HomeViewModel(
                                        classRepository,
                                        authRepository,
                                        navController
                                    )
                                }
                            )
                            HomeScreen(navController = navController, vm = homeViewModel)
                        }
                        composable(route = "${Screen.ClassScreen}/{classId}/{className}/{classMakerId}") {
                            val classId = remember {
                                it.arguments?.getString("classId")
                            }
                            val className = remember {
                                it.arguments?.getString("className")
                            }
                            val classMakerId = remember {
                                it.arguments?.getString("classMakerId")
                            }
                            val taskRepository = TaskRepository(authorizedNugazlahApi)
                            val classViewModel = viewModel<ClassViewModel>(
                                factory = vmFactoryHelper {
                                    ClassViewModel(
                                        className ?: "",
                                        classId ?: "",
                                        classMakerId ?: "",
                                        taskRepository,
                                        authRepository,
                                        scheduler
                                    )
                                }
                            )
                            ClassScreen(
                                navController = navController,
                                vm = classViewModel
                            )
                        }
                        composable(route = "${Screen.DetailTaskScreen}/{taskId}") {
                            val taskId = remember {
                                it.arguments?.getString("taskId")
                            }

                            val taskRepository = TaskRepository(authorizedNugazlahApi)
                            val taskDetailViewModel = viewModel<TaskDetailViewModel>(
                                factory = vmFactoryHelper {
                                    TaskDetailViewModel(
                                        taskRepository = taskRepository,
                                        navController = navController,
                                        taskId = taskId ?: ""
                                    )
                                }
                            )
                            DetailTaskScreen(
                                navController = navController,
                                vm = taskDetailViewModel,
                            )
                        }
                        composable(route = "${Screen.AddTaskScreen}/{classId}") {
                            val classId = remember {
                                it.arguments?.getString("classId")
                            }

                            val token = runBlocking {
                                nugazlahDatabase.tokenDao().get()
                            }

                            val taskRepository = TaskRepository(authorizedNugazlahApi)
                            val addTaskViewModel = viewModel<AddTaskViewModel>(
                                factory = vmFactoryHelper {
                                    AddTaskViewModel(
                                        taskRepository = taskRepository,
                                        navController = navController,
                                        classId = classId ?: ""
                                    )
                                }
                            )
                            AddTaskScreen(
                                navController = navController,
                                vm = addTaskViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
