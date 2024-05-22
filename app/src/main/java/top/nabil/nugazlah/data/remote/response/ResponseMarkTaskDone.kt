package top.nabil.nugazlah.data.remote.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseMarkTaskDone(
    @SerialName("code")
    val code: String,
    @SerialName("data")
    val `data`: Data?,
    @SerialName("message")
    val message: String
)