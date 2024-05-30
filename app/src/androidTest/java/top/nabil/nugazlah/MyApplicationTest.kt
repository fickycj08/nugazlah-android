package top.nabil.nugazlah

import androidx.compose.runtime.remember
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasImeAction
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.runBlocking
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
import kotlin.random.Random

class MyApplicationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @OptIn(ExperimentalTestApi::class)
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
                        val taskRepository =
                            TaskRepository(authorizedNugazlahApi, nugazlahDatabase.taskDao())
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

                        val taskRepository =
                            TaskRepository(authorizedNugazlahApi, nugazlahDatabase.taskDao())
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

                        val taskRepository =
                            TaskRepository(authorizedNugazlahApi, nugazlahDatabase.taskDao())
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

        val n = Random.nextInt(100, 1000)

        composeTestRule.onNodeWithText("Daftar").performClick()
        composeTestRule.onNodeWithText("Nama").assertIsDisplayed()
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
        composeTestRule.onNodeWithTag("InputNameRegister").performTextInput("M Nailan Nabil")
        composeTestRule.onNodeWithTag("InputEmailRegister")
            .performTextInput("nailanmnabil$n@gmail.com")
        composeTestRule.onNode(
            hasSetTextAction()
                    and
                    hasImeAction(
                        actionType = ImeAction.Done
                    )
        ).performTextInput("nailanmnabil")
        composeTestRule.onNodeWithText("Gasin").performClick()

        composeTestRule.onNodeWithText(
            "\uD83D\uDCE4",
            ignoreCase = true
        ).performClick()
        composeTestRule.onNodeWithText("Gasin").performClick()

        composeTestRule.onNodeWithText("Masuk").performClick()
        composeTestRule.onNodeWithText("Email").assertIsDisplayed()
        composeTestRule.onNodeWithText("Password").assertIsDisplayed()
        composeTestRule.onNode(
            hasSetTextAction()
                    and
                    hasImeAction(
                        actionType = ImeAction.Next
                    )
        ).performTextInput("nailanmnabil$n@gmail.com")
        composeTestRule.onNode(
            hasSetTextAction()
                    and
                    hasImeAction(
                        actionType = ImeAction.Done
                    )
        ).performTextInput("nailanmnabil")
        composeTestRule.onNodeWithText("Gasin").performClick()

        composeTestRule.onNode(
            hasTestTag("ActionButtonAddClass"),
            useUnmergedTree = true
        ).performClick()
        composeTestRule.onNodeWithText("Buat kelas").performClick()
        composeTestRule.onNodeWithTag("InputLecturerName").performTextInput("Lorem ipsum dolor")
        composeTestRule.onNodeWithTag("InputClassName")
            .performTextInput("Lorem ipsum")
        composeTestRule.onNodeWithTag("InputDescriptionClass")
            .performTextInput("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam accumsan sollicitudin lobortis. Aliquam sit amet tristique dui. Aenean tellus orci, congue vitae nunc in, posuere laoreet mi. Sed a dictum turpis. Etiam id lacus neque")
        composeTestRule.onNodeWithTag("InputIconClass").performTextInput("✅")
        composeTestRule.onNodeWithText("Gasin").performClick()

        composeTestRule.onNode(
            hasTestTag("ActionButtonAddClass"),
            useUnmergedTree = true
        ).performClick()
        composeTestRule.onNodeWithText("Gabung kelas", ignoreCase = true).performClick()
        composeTestRule.onNode(
            hasSetTextAction()
        ).performTextInput("QFFNRO")
        composeTestRule.onNodeWithText("Gasin").performClick()

        composeTestRule.onNodeWithTag("ClassCardTag").performClick()
        composeTestRule.onNodeWithText("There is no tasks", ignoreCase = true).assertIsDisplayed()

        composeTestRule.onNode(
            hasTestTag("ActionButtonAddTask"),
            useUnmergedTree = true
        ).performClick()
        composeTestRule.onNodeWithText("Tambah tugas", ignoreCase = true).assertIsDisplayed()
        composeTestRule.onNodeWithTag("InputTitleTaskTag").performTextInput("Lorem ipsum dolor")
        composeTestRule.onNodeWithTag("InputDescriptionTaskTag")
            .performTextInput("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam accumsan sollicitudin lobortis. Aliquam sit amet tristique dui. Aenean tellus orci, congue vitae nunc in, posuere laoreet mi. Sed a dictum turpis. Etiam id lacus neque")
        composeTestRule.onNodeWithTag("InputDetailTaskTag").performTextInput("Lorem ipsum dolor")
        composeTestRule.onNodeWithTag("InputSubmissionTaskTag")
            .performTextInput("Lorem ipsum dolor")
        composeTestRule.onNodeWithTag("InputDateTaskTag").performClick()

        composeTestRule.onNodeWithText("Friday, May 31, 2024", ignoreCase = true).performClick()
        composeTestRule.onNodeWithText("OK", ignoreCase = true).performClick()
        composeTestRule.onNodeWithText("OK", ignoreCase = true).performClick()
        composeTestRule.onNodeWithText("Gasin").performClick()

        composeTestRule.onNodeWithTag("TaskCardTag").performClick()
        composeTestRule.onNodeWithText(
            "☑\uFE0F",
            ignoreCase = true
        ).performClick()
    }
}
