package top.nabil.nugazlah

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasImeAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.text.input.ImeAction
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import top.nabil.nugazlah.alarm.Scheduler
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
import top.nabil.nugazlah.util.vmFactoryHelper
import java.util.regex.Matcher

class MyApplicationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testWholeApp() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            val nugazlahDatabase by lazy { NugazlahDatabase.getInstance(ApplicationProvider.getApplicationContext()) }
            val nugazlahApi by lazy { ServerConnect.getInstance() }
            val authRepository = AuthRepository(nugazlahApi, nugazlahDatabase.tokenDao())

            NugazlahTheme {
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
                val scheduler by lazy { Scheduler(ApplicationProvider.getApplicationContext()) }

                NavHost(
                    navController = navController,
                    startDestination = Screen.LoginScreen
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

        composeTestRule.onNodeWithText("Masuk").performClick()
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
        composeTestRule.onNode(
            hasSetTextAction()
                    and
                    hasImeAction(
                        actionType = ImeAction.Next
                    )
        ).performTextInput("nailanmnabil@gmail.com")
        composeTestRule.onNode(
            hasSetTextAction()
                    and
                    hasImeAction(
                        actionType = ImeAction.Done
                    )
        ).performTextInput("nailanmnabil")
        composeTestRule.onNodeWithText("Gasin").performClick()
        composeTestRule.onNodeWithTag("ActionButtonAddClass").performClick()
        composeTestRule.onNodeWithText("Buat Kelas", ignoreCase = true).performClick()
        composeTestRule.onNodeWithTag("InputLecturerName").performTextInput("Lorem ipsum dolor")
        composeTestRule.onNodeWithTag("InputClassName")
            .performTextInput("Lorem ipsum dolor sit amet")
        composeTestRule.onNodeWithTag("InputDescriptionClass")
            .performTextInput("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam accumsan sollicitudin lobortis. Aliquam sit amet tristique dui. Aenean tellus orci, congue vitae nunc in, posuere laoreet mi. Sed a dictum turpis. Etiam id lacus neque")
        composeTestRule.onNodeWithTag("InputIconClass").performTextInput("\uD83D\uDD25")
    }
}
