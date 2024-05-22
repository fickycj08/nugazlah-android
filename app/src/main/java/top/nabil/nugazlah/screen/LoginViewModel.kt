package top.nabil.nugazlah.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import top.nabil.nugazlah.data.local.Token
import top.nabil.nugazlah.data.remote.request.LoginRequest
import top.nabil.nugazlah.data.remote.request.RegisterRequest
import top.nabil.nugazlah.repository.AuthRepository
import top.nabil.nugazlah.util.Resource

data class LoginScreenState(
    val isLogin: Boolean = false,
    val isRegister: Boolean = false,
    val loginEmail: String = "",
    val loginEmailError: String = "",
    val loginPassword: String = "",
    val registerEmail: String = "",
    val registerPassword: String = "",
    val registerFullName: String = "",
    val isPasswordShown: Boolean = false,
    val isLoggedIn: Boolean = false
)

sealed class LoginScreenEvent {
    data class ShowToast(val message: String) : LoginScreenEvent()
}

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val navController: NavController
) : ViewModel() {
    private val _state = MutableStateFlow(LoginScreenState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<LoginScreenEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var isLoading by mutableStateOf(false)

    init {
        isAlreadyLoggedIn()
    }

    fun onStateChange(state: LoginScreenState) {
        _state.update { state }
    }

    private fun isAlreadyLoggedIn() {
        viewModelScope.launch {
            when (val result = authRepository.isLoggedIn()) {
                is Resource.Success -> {
                    _state.update { it.copy(isLoggedIn = result.data!!) }
                }

                is Resource.Error -> {
                    _eventFlow.emit(LoginScreenEvent.ShowToast(result.message!!))
                }
            }
        }
    }

    fun login() {
        validateLoginInput()
        val value = _state.value
        viewModelScope.launch {
            isLoading()
            val response =
                authRepository.login(LoginRequest(value.loginEmail, value.loginPassword))
            val responseData = response.data
            when (response) {
                is Resource.Success -> {
                    authRepository.saveToken(
                        Token(
                            token = responseData?.data?.accessToken ?: "",
                            userId = responseData?.data?.userId ?: ""
                        )
                    )
                    _state.update { it.copy(isLogin = false) }
                    _eventFlow.emit(LoginScreenEvent.ShowToast("Login success"))
                    navController.navigate(Screen.HomeScreen) {
                        popUpTo(Screen.LoginScreen) { inclusive = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }

                is Resource.Error -> {
                    _eventFlow.emit(LoginScreenEvent.ShowToast(response.message!!))
                }
            }
            isNotLoading()
        }
    }

    fun register() {
        val value = _state.value
        viewModelScope.launch {
            isLoading()
            val response =
                authRepository.register(
                    RegisterRequest(
                        value.registerEmail,
                        value.registerFullName,
                        value.registerPassword
                    )
                )
            val responseData = response.data
            when (response) {
                is Resource.Success -> {
                    authRepository.saveToken(
                        Token(
                            token = responseData?.data?.accessToken ?: "",
                            userId = responseData?.data?.userId ?: ""
                        )
                    )
                    _eventFlow.emit(LoginScreenEvent.ShowToast("Register success"))
                    navController.navigate(Screen.HomeScreen)
                }

                is Resource.Error -> {
                    _eventFlow.emit(LoginScreenEvent.ShowToast(response.message!!))
                }
            }
            isNotLoading()
        }
    }

    private fun validateLoginInput() {
        // TODO implement it
    }

    private fun validateRegisterInput() {
        // TODO implement it
    }

    private fun isLoading() {
        isLoading = true
    }

    private fun isNotLoading() {
        isLoading = false
    }
}