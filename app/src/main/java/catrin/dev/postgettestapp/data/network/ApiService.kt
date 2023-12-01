package catrin.dev.postgettestapp.data.network

import catrin.dev.postgettestapp.data.model.PaymentsResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.Call
import retrofit2.http.Header


interface ApiService {
    @Headers(
        "Accept: application/json",
        "app-key: 12345",
        "v: 1"
    )
    @GET("payments")
    fun getUserPayments(@Header("token") token: String): Call<PaymentsResponse>
}