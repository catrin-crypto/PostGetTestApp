package catrin.dev.postgettestapp.presentation.payments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import catrin.dev.postgettestapp.domain.Payment
import catrin.dev.postgettestapp.R.layout.payments_recyclerview_item

class RvAdapter(private val paymentsList: List<Payment>) :
    RecyclerView.Adapter<PaymentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder =
        PaymentViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(payments_recyclerview_item, parent, false)
        )

    override fun getItemCount(): Int = paymentsList.size

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        holder.bind(paymentsList[position])
    }
}