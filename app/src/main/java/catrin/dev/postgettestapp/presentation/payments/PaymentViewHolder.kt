package catrin.dev.postgettestapp.presentation.payments

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import catrin.dev.postgettestapp.databinding.PaymentsRecyclerviewItemBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import catrin.dev.postgettestapp.domain.Payment

class PaymentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val viewBinding: PaymentsRecyclerviewItemBinding by viewBinding()
    fun bind(
        payment: Payment,
    ) {
        with(viewBinding) {
            paymentIdTv.text =  payment.id.toString()
            paymentTitleTv.text = payment.title
            paymentAmountTv.text = buildString {
                append("Amount: ")
                append(payment.amount ?: " ")
            }
            paymentCreatedTv.text = buildString {
                append("Created: ")
                append(payment.created?.toString() ?: " ")
            }
        }
    }
}