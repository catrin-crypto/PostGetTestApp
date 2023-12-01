package catrin.dev.postgettestapp.presentation.main

import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import catrin.dev.postgettestapp.R
import catrin.dev.postgettestapp.data.network.NetworkTester
import catrin.dev.postgettestapp.databinding.FragmentLoginBinding
import catrin.dev.postgettestapp.helpers.click

class LoginFragment : Fragment() {
    private val binding: FragmentLoginBinding by viewBinding()
    private val mainViewModel: MainViewModel by activityViewModels()
    lateinit var networkTester: NetworkTester

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        networkTester = NetworkTester(
            requireContext()
                .applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        )
        binding.loginBtn.click {
            if (!networkTester.isNetworkAvailable()) {
                AlertDialog.Builder(requireContext() as MainActivity)
                    .setMessage(getString(R.string.login_fragment_alertdialog_msg))
                    .setPositiveButton(getString(R.string.login_fragment_alertdialog_positive_btn)) { dialog, _ -> dialog.cancel() }
                    .setCancelable(true)
                    .create().show()
            } else {
                val login = getUserLogin()
                val password = getUserPassword()
                if (login.isNotBlank() && password.isNotBlank())
                    mainViewModel.getLoggedInUserToken(login, password)
            }
        }
    }

    private fun getUserLogin(): String {
        with(binding) {
            val login = loginEdittext.text.trim().toString()
            if (login.isNotBlank()) {
                return login
            } else {
                Toast.makeText(
                    requireContext().applicationContext,
                    getString(R.string.login_edittext_toast_msg),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return ""
    }

    private fun getUserPassword(): String {
        with(binding) {
            val password = passwordEdittext.text.toString()
            if (password.isNotBlank()) {
                return password
            } else {
                Toast.makeText(
                    requireContext().applicationContext,
                    getString(R.string.password_edittext_toast_msg),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return ""
    }
}