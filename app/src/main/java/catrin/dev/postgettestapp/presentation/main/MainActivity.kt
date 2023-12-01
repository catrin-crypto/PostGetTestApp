package catrin.dev.postgettestapp.presentation.main

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import catrin.dev.postgettestapp.R
import catrin.dev.postgettestapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityMainBinding.inflate(layoutInflater)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setContentView(binding.root)
            initNavController()
            mainViewModel.screenState.observe(this) { screenState ->
                processScreenState(screenState)
            }
        } catch (e: Throwable) {
            logAndToast(e)
        }
    }

    private fun initNavController() {
        try {
            navController = findNavController(R.id.nav_host_fragment_activity_main)
        } catch (t: Throwable) {
            logAndToast(t)
        }
    }

    private fun processScreenState(screenState: ScreenState) {
        try {
            when (screenState) {
                is ScreenState.NotAuthorized -> {
                    navController.navigate(R.id.loginFragment)
                }

                is ScreenState.Authorized -> {
                    mainViewModel.loadUserPayments()
                }

                is ScreenState.GotPayments -> {
                    navController.navigate(R.id.paymentsFragment)
                }

                is ScreenState.RemoteError -> {
                    AlertDialog.Builder(this)
                        .setMessage("Server Error:${screenState.serverErrorResponse?.toString()}")
                        .setPositiveButton("OK") { dialog, _ -> dialog.cancel() }
                        .setCancelable(true)
                        .create().show()
                }

                is ScreenState.Error -> {
                    logAndToast(screenState.error)
                }
            }
        } catch (t: Throwable) {
            mainViewModel.handleError(t)
        }
    }

    private fun logAndToast(t: Throwable) = logAndToast(t, this::class.java.toString())

    private fun logAndToast(t: Throwable, TAG: String) {
        try {
            Log.e(TAG, "", t)
            Toast.makeText(applicationContext, t.toString(), Toast.LENGTH_LONG).show()
        } catch (ex: Throwable) {
            Log.e("UE", "Unexpected exception in logAndToast exception handler", ex)
        }
    }
}