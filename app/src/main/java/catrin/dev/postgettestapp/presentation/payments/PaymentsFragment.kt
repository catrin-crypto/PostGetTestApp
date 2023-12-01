package catrin.dev.postgettestapp.presentation.payments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import catrin.dev.postgettestapp.R
import catrin.dev.postgettestapp.databinding.FragmentPaymentsBinding
import catrin.dev.postgettestapp.domain.Payment
import catrin.dev.postgettestapp.helpers.click
import catrin.dev.postgettestapp.presentation.main.MainViewModel
import catrin.dev.postgettestapp.presentation.main.ScreenState

class PaymentsFragment : Fragment() {
    private val binding: FragmentPaymentsBinding by viewBinding()
    private var adapter: RvAdapter? = null
    private val mainViewModel: MainViewModel by activityViewModels()
    private val storedPayments: MutableList<Payment> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_payments, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter =
            RvAdapter(storedPayments)
        adapter!!.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        with(binding) {
            paymentsRv.adapter = adapter
            paymentsRv.isSaveEnabled = true
            logoutFab.click {
               mainViewModel.logout()
            }
        }
        mainViewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            processScreenState(
                screenState
            )
        }
    }

    private fun processScreenState(screenState: ScreenState) {
        try {
            when (screenState) {
                is ScreenState.GotPayments -> {
                    showPayments(screenState.payments)
                }

                else -> {}
            }
        } catch (t: Throwable) {
            mainViewModel.handleError(t)
        }
    }

    private fun showPayments(payments: List<Payment>) {
        val diffResult = DiffUtil.calculateDiff(PaymentsDiff(storedPayments, payments))
        storedPayments.clear()
        storedPayments.addAll(payments)
        adapter?.let { diffResult.dispatchUpdatesTo(it) }
    }

    override fun onDestroy() {
        adapter = null
        super.onDestroy()
    }
}