package catrin.dev.postgettestapp.data.network

import catrin.dev.postgettestapp.data.model.LoginRequest
import catrin.dev.postgettestapp.data.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginService {
    @Headers(
        "Accept: application/json",
        "app-key: 12345",
        "v: 1"
    )
    @POST("login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
}