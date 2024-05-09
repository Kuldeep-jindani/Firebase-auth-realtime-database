package com.kuldeep.pestopractical.ui.SplashScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.kuldeep.pestopractical.R
import com.kuldeep.pestopractical.databinding.ActivitySplashScreenBinding
import com.kuldeep.pestopractical.ui.DashBoard.DashboardActivity
import com.kuldeep.pestopractical.ui.LoginActivity.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {

    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        startLoginScreen()
    }

    private fun startLoginScreen() {
        lifecycleScope.launch {
            delay(3000)
            splashViewModel.startLoginActivity().collect{
                updateOnEvent(it)
            }
        }
    }

    fun updateOnEvent(
        splashEvents: SplashEvents
    ) {
        when (splashEvents) {
            is SplashEvents.showToast -> {
                Toast.makeText(this, splashEvents.message, Toast.LENGTH_SHORT).show()
            }

            SplashEvents.startLoginActivity -> {
                intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}