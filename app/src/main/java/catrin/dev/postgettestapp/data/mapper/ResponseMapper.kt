package catrin.dev.postgettestapp.data.mapper

import catrin.dev.postgettestapp.data.model.PaymentDto
import catrin.dev.postgettestapp.domain.Payment

class ResponseMapper {
    fun mapPaymentResponseToPayment(paymentsResponse: List<PaymentDto>): List<Payment> {
        val res = mutableListOf<Payment>()
        for (item in paymentsResponse) {
            val payment = Payment(
                id = item.id,
                title = item.title,
                amount = item.amount,
                created = item.created
            )
            res.add(payment)
        }
        return res
    }
}