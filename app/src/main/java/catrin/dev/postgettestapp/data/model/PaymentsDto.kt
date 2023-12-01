package catrin.dev.postgettestapp.data.model

import com.google.gson.annotations.SerializedName

data class PaymentsResponse(
    @SerializedName("success") val success : Boolean,
    @SerializedName("response") val response : List<PaymentDto>,
    @SerializedName("error") val remoteError: ServerErrorResponse
)
data class PaymentDto(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("amount") val amount: String?,
    @SerializedName("created") val created: Long?
)

