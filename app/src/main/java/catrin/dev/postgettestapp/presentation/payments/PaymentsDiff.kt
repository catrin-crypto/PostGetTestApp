package catrin.dev.postgettestapp.presentation.payments

import androidx.recyclerview.widget.DiffUtil
import catrin.dev.postgettestapp.domain.Payment

class PaymentsDiff(private val oldList: List<Payment>, private val newList: List<Payment>) :
    DiffUtil.Callback() {
    private val payload = Any()
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return try {
            oldList[oldItemPosition].id == newList[newItemPosition].id
        } catch (e: Throwable) {
            false
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return try {
            val oldItem: Payment = oldList[oldItemPosition]
            val newItem: Payment = newList[newItemPosition]
            oldItem.id == newItem.id &&
                    oldItem.title == newItem.title &&
                    oldItem.amount == newItem.amount &&
                    oldItem.created == newItem.created
        } catch (e: Throwable) {
            false
        }
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int) = payload

}