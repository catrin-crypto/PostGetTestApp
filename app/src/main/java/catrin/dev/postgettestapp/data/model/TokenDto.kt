package catrin.dev.postgettestapp.data.model

import com.google.gson.annotations.SerializedName

data class TokenDto(
    @SerializedName("token") val token: String
)

data class LoginRequest(
    @SerializedName("login") val login: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("response") val response: TokenDto,
    @SerializedName("error") val remoteError: ServerErrorResponse
)

data class ServerErrorResponse(
    @SerializedName("error_code") val errorCode: String,
    @SerializedName("error_msg") val errorMsg: String
)