package com.kuldeep.pestopractical.ui.RegisterActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import com.kuldeep.pestopractical.data.util.ResultOf
import com.kuldeep.pestopractical.databinding.ActivityRegisterBinding
import com.kuldeep.pestopractical.ui.DashBoard.DashboardActivity
import com.kuldeep.pestopractical.ui.LoginActivity.FireBaseViewModel
import com.kuldeep.pestopractical.ui.LoginActivity.LoginActivity
import kotlinx.coroutines.launch


class RegisterActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterBinding? = null
    private val binding get() = _binding!!
    private val mAuth = FirebaseAuth.getInstance()

    private val registerViewModel : RegisterViewModel by viewModels()
    private val fireBaseViewModel: FireBaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
    }

    private fun initListeners() {
        // Login Textview Click
        binding.idTVLoginUser.setOnClickListener {
//            intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
            onLoginBtnClick()
        }

        // Register Button click
        binding.idBtnRegister.setOnClickListener {
            binding.idPBLoading.visibility = View.VISIBLE
            val database = Firebase.database
            val myRef = database.getReference("message")
            val userName: String = binding.idEdtUserName.text.toString()
            val pwd: String = binding.idEdtPassword.text.toString()
            val cnfPwd: String = binding.idEdtConfirmPassword.text.toString()

            if (userName.isEmpty()) {
                binding.idEdtUserName.error = "Username must not be empty"
            } else if (pwd.isEmpty()) {
                binding.idEdtPassword.error = "Password must not be empty"
            } else if (cnfPwd.isEmpty()) {
                binding.idEdtConfirmPassword.error = "Confirm Password must not be empty"
            } else if (!pwd.equals(cnfPwd)) {
//                Toast.makeText(
//                    applicationContext,
//                    "Password and Confirm password should be same!!",
//                    Toast.LENGTH_LONG
//                ).show()
                showToastToViewmodel("Password and Confirm password should be same!!")
            } else {
                fireBaseViewModel.signUp(userName,pwd)
                /*mAuth.createUserWithEmailAndPassword(userName, pwd).addOnCompleteListener { task ->
                    // on below line we are checking if the task is success or not.
                    if (task.isSuccessful) {

                        // in on success method we are hiding our progress bar and opening a login activity.
                        binding.idPBLoading.setVisibility(View.GONE)
                        Toast.makeText(
                            this@RegisterActivity,
                            "User Registered..",
                            Toast.LENGTH_SHORT
                        ).show()
                        val i = Intent(
                            this@RegisterActivity,
                            LoginActivity::class.java
                        )
                        startActivity(i)
                        finish()
                    } else {

                        // in else condition we are displaying a failure toast message.
                        binding.idPBLoading.setVisibility(View.GONE)
                        Toast.makeText(
                            this@RegisterActivity,
                            "Fail to register user..",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }*/
                fireBaseViewModel.registrationStatus.observe(this, Observer {
                    when(it){
                        is ResultOf.Success ->{
                            afterLoginEvents()
                        }
                        is ResultOf.Failure -> {
                            val failedMessage =  it.message ?: "Unknown Error"
                            println(failedMessage)
                            showToastToViewmodel("Registration failed with $failedMessage")
                        }
                    }
                })
            }
        }
    }

    fun updateViewOnEvent(
        registerEvents: RegisterEvents
    ){
        when(registerEvents){
            is RegisterEvents.showToast -> {
                Toast.makeText(
                    this@RegisterActivity,
                    registerEvents.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

            is RegisterEvents.hideLoading -> binding.idPBLoading.visibility = View.GONE
            is RegisterEvents.onLoginClick -> {
                val i = Intent(
                    this@RegisterActivity,
                    LoginActivity::class.java
                )
                startActivity(i)
                finish()
            }
            is RegisterEvents.showLoading -> binding.idPBLoading.visibility = View.VISIBLE
            is RegisterEvents.startDashboardActivity -> {
                val i = Intent(
                    this@RegisterActivity,
                    DashboardActivity::class.java
                )
                startActivity(i)
                finish()
            }
        }
    }

    fun showToastToViewmodel(
        message : String
    ){
        lifecycleScope.launch {
            registerViewModel.showToastToViewmodel(message).collect {
                updateViewOnEvent(it)
            }
        }
    }

    fun onLoginBtnClick() {
        lifecycleScope.launch {
            registerViewModel.onRegisterBtnClick().collect {
                updateViewOnEvent(it)
            }
        }
    }

    fun afterLoginEvents() {
        lifecycleScope.launch {
            registerViewModel.afterLoginEvents().collect {
                updateViewOnEvent(it)
            }
        }
    }
}