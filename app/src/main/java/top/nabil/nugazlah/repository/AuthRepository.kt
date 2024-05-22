package top.nabil.nugazlah.repository

import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import top.nabil.nugazlah.data.local.Token
import top.nabil.nugazlah.data.remote.NugazlahApi
import top.nabil.nugazlah.data.remote.request.LoginRequest
import top.nabil.nugazlah.data.remote.request.RegisterRequest
import top.nabil.nugazlah.data.remote.response.LoginResponse
import top.nabil.nugazlah.data.remote.response.RegisterResponse
import top.nabil.nugazlah.util.parseErrorResponse
import top.nabil.nugazlah.data.local.TokenDao
import top.nabil.nugazlah.util.ErrorMessage
import top.nabil.nugazlah.util.Resource

class AuthRepository(
    private val nugazlahApi: NugazlahApi,
    private val tokenDao: TokenDao,
) {
    suspend fun getUserId(): Resource<String> {
        return try {
            val token = this.tokenDao.get()
            if (token.id != 0) {
                return Resource.Success(token.userId)
            }
            return Resource.Success("")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Success("")
        }
    }

    suspend fun isLoggedIn(): Resource<Boolean> {
        return try {
            val token = this.tokenDao.get()
            if (token != null) {
                if (token.id != 0) {
                    return Resource.Success(true)
                }
            }
            return Resource.Success(false)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Success(false)
        }
    }

    suspend fun saveToken(token: Token): Resource<String> {
        return try {
            this.tokenDao.insert(token)
            Resource.Success("")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(ErrorMessage.applicationError)
        }
    }

    suspend fun login(request: LoginRequest): Resource<LoginResponse?> {
        return try {
            val response = nugazlahApi.login(request)
            Resource.Success(response)
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> {
                    e.printStackTrace()
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = errorBody?.let { json ->
                        parseErrorResponse(json)
                    }
                    if (errorResponse?.code == "C-0005" || errorResponse?.code == "C-0004") {
                        return Resource.Error("Wrong password or email")
                    }
                    Resource.Error(e.message())
                }

                500 -> {
                    e.printStackTrace()
                    Resource.Error(ErrorMessage.applicationError)
                }

                else -> {
                    e.printStackTrace()
                    Resource.Error(ErrorMessage.applicationError)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(ErrorMessage.applicationError)
        }
    }

    suspend fun register(request: RegisterRequest): Resource<RegisterResponse?> {
        return try {
            val response = nugazlahApi.register(request)
            Resource.Success(response)
        } catch (e: HttpException) {
            when (e.code()) {
                400 -> {
                    e.printStackTrace()
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = errorBody?.let { json ->
                        parseErrorResponse(json)
                    }
                    if (errorResponse?.code == "C-0005") {
                        return Resource.Error("Please check your input and try again.")
                    }
                    if (errorResponse?.code == "C-0003") {
                        return Resource.Error("The email address is already in use.")
                    }
                    Resource.Error(e.message())
                }

                500 -> {
                    e.printStackTrace()
                    Resource.Error(ErrorMessage.applicationError)
                }

                else -> {
                    e.printStackTrace()
                    Resource.Error(ErrorMessage.applicationError)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(ErrorMessage.applicationError)
        }
    }

    suspend fun logout(): Resource<String> {
        return try {
            this.tokenDao.delete()
            Resource.Success("")
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(ErrorMessage.applicationError)
        }
    }
}
