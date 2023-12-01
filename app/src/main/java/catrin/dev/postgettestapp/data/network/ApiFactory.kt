package catrin.dev.postgettestapp.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiFactory {
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://easypay.world/api-test/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)

        .build()

    val apiService: ApiService = retrofit.create()
    val loginService: LoginService = retrofit.create()
}