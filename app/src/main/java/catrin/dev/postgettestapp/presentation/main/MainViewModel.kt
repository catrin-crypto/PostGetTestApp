package catrin.dev.postgettestapp.presentation.main

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import catrin.dev.postgettestapp.data.mapper.ResponseMapper
import catrin.dev.postgettestapp.data.model.LoginRequest
import catrin.dev.postgettestapp.data.model.LoginResponse
import catrin.dev.postgettestapp.data.model.PaymentsResponse
import catrin.dev.postgettestapp.data.model.ServerErrorResponse
import catrin.dev.postgettestapp.data.network.ApiFactory.apiService
import catrin.dev.postgettestapp.data.network.ApiFactory.loginService
import catrin.dev.postgettestapp.domain.Payment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences =
        application.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
    var currentServerToken: String? = sharedPreferences.getString("token", null)

    private val _screenState = MutableLiveData<ScreenState>(
        (if (currentServerToken != null) {
            ScreenState.Authorized(currentServerToken.toString())
        } else ScreenState.NotAuthorized)
    )
    val screenState: LiveData<ScreenState> = _screenState

    private val mapper = ResponseMapper()
    private val _currentPayments = mutableListOf<Payment>()
    val currentPayments: List<Payment>
        get() = _currentPayments.toList()

    fun getLoggedInUserToken(login: String, password: String) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    requestUserToken(login, password)
                } catch (e: Throwable) {
                    handleError(e)
                }
            }
        } catch (e: Throwable) {
            handleError(e)
        }
    }

    fun saveServerToken() {
        sharedPreferences.edit()
            .putString("token", currentServerToken)
            .apply()
    }

    private fun requestUserToken(login: String, password: String) {
        try {
            val loginRequest = LoginRequest(login, password)
            val call = loginService.login(loginRequest)
            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    try {
                        if (response.isSuccessful) {
                            val serverSuccess = response.body()?.success
                            val token = response.body()?.response?.token
                            if (serverSuccess != true) {
                                _screenState.postValue(
                                    ScreenState
                                        .RemoteError(response.body()?.remoteError)
                                )
                                Log.d(
                                    "requestUserToken",
                                    "Server returned error: ${response.body()?.toString()}"
                                )
                            } else {
                                if (token.isNullOrBlank()) {
                                    _screenState.postValue(
                                        ScreenState
                                            .RemoteError(
                                                ServerErrorResponse(
                                                    "",
                                                    "Server returned blank token!"
                                                )
                                            )
                                    )
                                    Log.d("requestUserToken", "Server returned blank token!")
                                } else {
                                    currentServerToken = token
                                    saveServerToken()
                                    _screenState.postValue(
                                        ScreenState
                                            .Authorized(token)
                                    )
                                    Log.d("requestUserToken", "got token: $token")
                                }
                            }
                        } else {
                            _screenState.postValue(
                                ScreenState
                                    .RemoteError(
                                        ServerErrorResponse(
                                            response.code().toString(),
                                            "Remote error: ${response.message()}"
                                        )
                                    )
                            )
                            Log.d("requestUserToken", "got error: ${response.message()}")
                        }
                    } catch (e: Throwable) {
                        handleError(e)
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    handleError(t)
                }
            })
        } catch (e: Throwable) {
            handleError(e)
        }
    }

    fun loadUserPayments() {
        try {
            if (!currentServerToken.isNullOrBlank()) {
                val call = apiService.getUserPayments(currentServerToken.toString())

                call.enqueue(object : Callback<PaymentsResponse> {
                    override fun onResponse(
                        call: Call<PaymentsResponse>,
                        response: Response<PaymentsResponse>
                    ) {
                        try {
                            if (response.isSuccessful) {
                                val serverSuccess = response.body()?.success

                                if (serverSuccess != true) {
                                    _screenState.postValue(
                                        ScreenState.RemoteError(
                                            response.body()?.remoteError
                                        )
                                    )
                                    Log.d(
                                        "requestUserPayments",
                                        "Server returned error: ${response.body()?.toString()}"
                                    )
                                } else {
                                    val payments = response.body()?.response?.let {
                                        mapper.mapPaymentResponseToPayment(
                                            it
                                        )
                                    }
                                    if (!payments.isNullOrEmpty()) {
                                        _currentPayments.addAll(payments)
                                        _screenState.postValue(
                                            ScreenState.GotPayments(
                                                currentPayments
                                            )
                                        )
                                    } else Log.d("Load Payments", "empty payments")
                                }
                            }
                        } catch (e: Throwable) {
                            handleError(e)
                        }
                    }

                    override fun onFailure(call: Call<PaymentsResponse>, t: Throwable) {
                        handleError(t)
                    }
                }
                )
            }
        } catch (t: Throwable) {
            handleError(t)
        }
    }

    fun logout() {
        currentServerToken = null
        saveServerToken()
        _screenState.postValue(ScreenState.NotAuthorized)
    }

    fun handleError(error: Throwable) {
        try {
            _screenState.postValue(ScreenState.Error(error))
        } catch (e: Throwable) {
            Log.e("UE", "Unexpected exception in handleError() exception handler", e)
        }
    }
}