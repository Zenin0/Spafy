package com.isanz.spafy.loginModule

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.isanz.spafy.MainActivity
import com.isanz.spafy.R
import com.isanz.spafy.common.retrofit.login.LoginService
import com.isanz.spafy.common.retrofit.login.UserLoginInfo
import com.isanz.spafy.common.retrofit.login.UserRegisterInfo
import com.isanz.spafy.common.utils.Constants
import com.isanz.spafy.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

            with(mBinding) {
                if (!checked) {
                    tilUsername.visibility = View.VISIBLE
                    tilBirth.visibility = View.VISIBLE
                    tilCountry.visibility = View.VISIBLE
                    tilPostalCode.visibility = View.VISIBLE
                    rgType.visibility = View.VISIBLE

                } else {

                    tilUsername.visibility = View.GONE
                    tilBirth.visibility = View.GONE
                    tilCountry.visibility = View.GONE
                    tilPostalCode.visibility = View.GONE
                    rgType.visibility = View.GONE

                }
            }

            mBinding.btnLogin.text = button.text
        }

        mBinding.btnLogin.setOnClickListener {
            if (mBinding.swType.isChecked) login()
            else register()
        }
    }

    private fun validateFields(): Boolean {
        with(mBinding) {
            if (swType.isChecked) {
                if (etEmail.text?.isEmpty() == true || etPassword.text?.isEmpty() == true) {
                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.login_error_empty_fields),
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
            } else {
                if (etEmail.text?.isEmpty() == true || etPassword.text?.isEmpty() == true || etUsername.text?.isEmpty() == true ||
                    etBirt.text?.isEmpty() == true || etCountry.text?.isEmpty() == true || etPostalCode.text?.isEmpty() == true ||
                    !android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString())
                        .matches() ||
                    !etBirt.text.toString().matches(Regex("\\d{2}/\\d{2}/\\d{4}"))
                ) {
                    Toast.makeText(
                        this@LoginActivity,
                        getString(R.string.login_error_invalid_fields),
                        Toast.LENGTH_SHORT
                    ).show()
                    return false
                }
            }
        }
        return true
    }

    private fun register() {
        if (validateFields()) {
            val email = mBinding.etEmail.text.toString().trim()
            val password = mBinding.etPassword.text.toString().trim()
            val username = mBinding.etUsername.text.toString().trim()
            val birth = mBinding.etBirt.text.toString().trim()
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
            val date: Date? = format.parse(birth)
            val country = mBinding.etCountry.text.toString().trim()
            val postalCode = mBinding.etPostalCode.text.toString().trim()
            val gender = if (mBinding.rbMale.isChecked) "M" else "F"

            val retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
            val loginService = retrofit.create(LoginService::class.java)

            lifecycleScope.launch {
                try {
                    val result = loginService.registerUser(
                        UserRegisterInfo(
                            username, password, email, gender, date.toString(), country, postalCode
                        )
                    )
                    if (result.ok.isNotEmpty()) goToMain(result.ok.toInt())
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
                                Log.i("Error", e.message.toString())
                            }
                        }
                    }
                }
            }
        }
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
                if (result.ok.isNotEmpty()) goToMain(result.ok.toInt())
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

    private fun goToMain(id: Int) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("id", id) // replace "key" and "value" with your actual key and value
        startActivity(intent)
    }
}