package com.isanz.spafy.loginModule

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.isanz.spafy.MainActivity
import com.isanz.spafy.R
import com.isanz.spafy.common.retrofit.login.UserLoginInfo
import com.isanz.spafy.common.retrofit.login.LoginService
import com.isanz.spafy.common.utils.Constants
import com.isanz.spafy.databinding.ActivityLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        setContentView(mBinding.root)
        setupButton()
    }

    private fun setupButton() {
        mBinding.swType.setOnCheckedChangeListener { button, checked ->
            button.text = if (checked) getString(R.string.main_login)
            else getString(R.string.main_type_register)
            mBinding.btnLogin.text = button.text
        }

        mBinding.btnLogin.setOnClickListener {
            if (mBinding.swType.isChecked) login()
            else register()
        }
    }

    private fun register() {
        TODO("Not yet implemented")
    }


    private fun login() {
        val email = mBinding.etEmail.text.toString().trim()
        val password = mBinding.etPassword.text.toString().trim()
        val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val loginService = retrofit.create(LoginService::class.java)

        lifecycleScope.launch {
            try {
                val result = loginService.logUser(
                    UserLoginInfo(
                        email, password
                    )
                )
                if (result.ok.isNotEmpty()) goToMain()
            } catch (e: Exception) {
                (e as? HttpException)?.let {
                    when (it.code()) {
                        400 -> {
                            Toast.makeText(
                                this@LoginActivity,
                                getString(R.string.login_error_bad_request),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        401 -> {
                            Toast.makeText(
                                this@LoginActivity,
                                getString(R.string.login_error_unauthorized),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            Toast.makeText(
                                this@LoginActivity,
                                getString(R.string.login_error_generic),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}