package catrin.dev.postgettestapp.presentation.main

import catrin.dev.postgettestapp.data.model.ServerErrorResponse
import catrin.dev.postgettestapp.domain.Payment

sealed class ScreenState{
    data class Authorized(val token:String): ScreenState()
    data class GotPayments(val payments:List<Payment>):ScreenState()
    data class Error(val error: Throwable):ScreenState()
    data class RemoteError(val serverErrorResponse: ServerErrorResponse?):ScreenState()
    object NotAuthorized: ScreenState()
}
