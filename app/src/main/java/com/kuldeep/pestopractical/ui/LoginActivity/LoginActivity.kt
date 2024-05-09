package com.kuldeep.pestopractical.ui.LoginActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.kuldeep.pestopractical.data.util.ResultOf
import com.kuldeep.pestopractical.databinding.ActivityLoginBinding
import com.kuldeep.pestopractical.ui.DashBoard.DashboardActivity
import com.kuldeep.pestopractical.ui.RegisterActivity.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels()
    private val fireBaseViewModel: FireBaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListener()
    }

    private fun initListener() {
        // click on new register textview
        binding.idTVNewUser.setOnClickListener {
            onRegisterBtnClick()
        }
        // click on login button
        binding.idBtnLogin.setOnClickListener {
            binding.idPBLoading.visibility = View.VISIBLE

            if (binding.idEdtUserName.text.toString().isEmpty()) {
                binding.idEdtUserName.error = "Username must not be empty"
            } else if (binding.idEdtPassword.text.toString().isEmpty()) {
                binding.idEdtPassword.error = "Password must not be empty"
            } else {
                fireBaseViewModel.signIn(
                    binding.idEdtUserName.text.toString(),
                    binding.idEdtPassword.text.toString()
                )
                fireBaseViewModel.signInStatus.observe(this, Observer {
                    when(it){
                        is ResultOf.Success ->{
                            if(it.value.equals("Login Successful",ignoreCase = true)){
                                // on below line we are hiding our progress bar.
                                afterLoginEvents()
                            }else if(it.value.equals("Reset",ignoreCase = true)){

                            }
                            else{
                                showToastToViewmodel("Login failed with ${it.value}")
                            }
                        }
                        is ResultOf.Failure -> {
                            val failedMessage =  it.message ?: "Unknown Error"
                            showToastToViewmodel("Login failed with $failedMessage")
                        }
                    }
                })
            }
        }
    }

    override fun onStart() {
        super.onStart()
        fireBaseViewModel.currentUser()
        fireBaseViewModel.currentUserStatus.observe(this, Observer {
            when(it){
                is ResultOf.Success ->{
                    afterLoginEvents()
                }
                is ResultOf.Failure -> {
                }
            }
        })

    }

    fun updateViewOnEvent(
        loginEvents: LoginEvents
    ) {
        when (loginEvents) {
            is LoginEvents.showToast -> {
                Toast.makeText(
                    this@LoginActivity,
                    loginEvents.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is LoginEvents.hideLoading -> {
                binding.idPBLoading.visibility = View.GONE
            }

            is LoginEvents.showLoading -> {
                binding.idPBLoading.visibility = View.VISIBLE
            }

            is LoginEvents.startLoginActivity -> {
                val i = Intent(
                    this@LoginActivity,
                    DashboardActivity::class.java
                )
                startActivity(i)
                finish()
            }

            is LoginEvents.currentUser -> {
                if (loginEvents.user != null) {
                    // if the user is not null then we are
                    // opening a main activity on below line.
                    val i = Intent(this@LoginActivity, DashboardActivity::class.java)
                    startActivity(i)
                    finish()
                }
            }

            is LoginEvents.onRegisterClick -> {
                intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun afterLoginEvents() {
        lifecycleScope.launch {
            loginViewModel.afterLoginEvents().collect {
                updateViewOnEvent(it)
            }
        }
    }

    fun onRegisterBtnClick() {
        lifecycleScope.launch {
            loginViewModel.onRegisterBtnClick().collect {
                updateViewOnEvent(it)
            }
        }
    }

    fun showToastToViewmodel(
        message : String
    ){
        lifecycleScope.launch {
            loginViewModel.showToastToViewmodel(message).collect {
                updateViewOnEvent(it)
            }
        }
    }
}